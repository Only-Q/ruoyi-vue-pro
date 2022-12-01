package cn.iocoder.yudao.module.system.service.upload;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.io.UnZipFiles;
import cn.iocoder.yudao.module.system.controller.admin.upload.AsyncParse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.system.controller.admin.upload.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.upload.UploadDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.system.convert.upload.UploadConvert;
import cn.iocoder.yudao.module.system.dal.mysql.upload.UploadMapper;
import org.springframework.web.multipart.MultipartFile;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * 文件上传 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Resource
    private UploadMapper uploadMapper;
    @Resource
    private AsyncParse asyncParse;

    private final String UPLOADPATH_LINUX = "/data/sxmu/uploadFile/";
    private final String UPLOADPATH_WINDOWS = "d:/sxmu/uploadFile/";

    @Override
    public Long createUpload(UploadCreateReqVO createReqVO) {
        // 插入
        UploadDO upload = UploadConvert.INSTANCE.convert(createReqVO);
        uploadMapper.insert(upload);
        // 返回
        return upload.getId();
    }

    @Override
    public void updateUpload(UploadUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateUploadExists(updateReqVO.getId());
        // 更新
        UploadDO updateObj = UploadConvert.INSTANCE.convert(updateReqVO);
        uploadMapper.updateById(updateObj);
    }

    @Override
    public void deleteUpload(Long id) {
        // 校验存在
        this.validateUploadExists(id);
        // 删除
        uploadMapper.deleteById(id);
    }

    private void validateUploadExists(Long id) {
        if (uploadMapper.selectById(id) == null) {
            throw exception(UPLOAD_NOT_EXISTS);
        }
    }

    @Override
    public UploadDO getUpload(Long id) {
        return uploadMapper.selectById(id);
    }

    @Override
    public List<UploadDO> getUploadList(Collection<Long> ids) {
        return uploadMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<UploadDO> getUploadPage(UploadPageReqVO pageReqVO) {
        return uploadMapper.selectPage(pageReqVO);
    }

    @Override
    public List<UploadDO> getUploadList(UploadExportReqVO exportReqVO) {
        return uploadMapper.selectList(exportReqVO);
    }

    @Override
    public CommonResult<String> importReport(MultipartFile file) {
        List<UploadDO> uploadDOS = uploadMapper.selectList(new UploadExportReqVO());
        List<UploadDO> collect = uploadDOS.stream().filter(f -> f.getAnalysisStatus() == 1).collect(Collectors.toList());
        if(collect.size() > 0){
            return CommonResult.error(9999,"还有zip包正在解析中,请稍后再试...");
        }
        // 插入
        UploadDO upload = new UploadDO();
        upload.setFileName( file.getOriginalFilename() );
        log.info("开始插入数据");
        uploadMapper.insert(upload);
        log.info("插入数据结束,id:{}",upload.getId().toString());
        log.info("开始上传zip文件");
        String zipUrl = uploadFileReturnUrl(file, upload.getId().toString());
        log.info("上传zip文件结束");
        try{
            log.info("开始解压zip文件");
            if ("linux".equalsIgnoreCase(System.getProperty("os.name"))) {
                UnZipFiles.unZip(zipUrl,UPLOADPATH_LINUX+upload.getId().toString()+"/");
            } else {
                UnZipFiles.unZip(zipUrl,UPLOADPATH_WINDOWS+upload.getId().toString()+"/");
            }
            log.info("解压zip文件结束");
            asyncParse.parsePdf(upload);
            return CommonResult.success("体检报告上传成功,请隔段时间来下载解析结果");
        }catch (Exception e){
            log.error(e.toString());
            return CommonResult.error(9999,e.getMessage());
        }
    }

    private String uploadFileReturnUrl(MultipartFile file,String id){
        String url=null;
        if (!file.isEmpty()) {
            String fileSavePath = "";
            if ("linux".equalsIgnoreCase(System.getProperty("os.name"))) {
                fileSavePath = UPLOADPATH_LINUX+id+"/";
            } else {
                fileSavePath = UPLOADPATH_WINDOWS+id+"/";
            }
            String fileName =file.getOriginalFilename();
            try {
                //判断文件存储目录是否存在，不存在则创建。注意文件目录不包括后面的文件名
                File filePath = new File(fileSavePath);
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }
                //服务器上文件存储文件地址和文件路径，注意加上后面的文件名
                String savePath = fileSavePath + fileName;
                File savePathFile = new File(savePath);
                //使用MultipartFile自带的方法转存文件到服务器,注意使用的是绝对路径
                file.transferTo(savePathFile.getAbsoluteFile());
                return savePath;
            } catch (Exception e) {
                log.error(e.getMessage());
                return null;
            }
        } else {
            return null;
        }

    }



}
