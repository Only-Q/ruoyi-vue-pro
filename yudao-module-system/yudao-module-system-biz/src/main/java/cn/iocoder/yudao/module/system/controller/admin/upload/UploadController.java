package cn.iocoder.yudao.module.system.controller.admin.upload;

import cn.iocoder.yudao.module.system.controller.admin.biologysample.vo.BaseInfoImportExcelVo;
import cn.iocoder.yudao.module.system.controller.admin.biologysample.vo.SampleImportExcelVo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.*;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.system.controller.admin.upload.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.upload.UploadDO;
import cn.iocoder.yudao.module.system.convert.upload.UploadConvert;
import cn.iocoder.yudao.module.system.service.upload.UploadService;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "管理后台 - 文件上传")
@RestController
@RequestMapping("/system/upload")
@Validated
public class UploadController {

    @Resource
    private UploadService uploadService;

    @PostMapping("/create")
    @ApiOperation("创建文件上传")
    @PreAuthorize("@ss.hasPermission('system:upload:create')")
    public CommonResult<Long> createUpload(@Valid @RequestBody UploadCreateReqVO createReqVO) {
        return success(uploadService.createUpload(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新文件上传")
    @PreAuthorize("@ss.hasPermission('system:upload:update')")
    public CommonResult<Boolean> updateUpload(@Valid @RequestBody UploadUpdateReqVO updateReqVO) {
        uploadService.updateUpload(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除文件上传")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('system:upload:delete')")
    public CommonResult<Boolean> deleteUpload(@RequestParam("id") Long id) {
        uploadService.deleteUpload(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得文件上传")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('system:upload:query')")
    public CommonResult<UploadRespVO> getUpload(@RequestParam("id") Long id) {
        UploadDO upload = uploadService.getUpload(id);
        return success(UploadConvert.INSTANCE.convert(upload));
    }

    @GetMapping("/list")
    @ApiOperation("获得文件上传列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('system:upload:query')")
    public CommonResult<List<UploadRespVO>> getUploadList(@RequestParam("ids") Collection<Long> ids) {
        List<UploadDO> list = uploadService.getUploadList(ids);
        return success(UploadConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得文件上传分页")
    @PreAuthorize("@ss.hasPermission('system:upload:query')")
    public CommonResult<PageResult<UploadRespVO>> getUploadPage(@Valid UploadPageReqVO pageVO) {
        PageResult<UploadDO> pageResult = uploadService.getUploadPage(pageVO);
        return success(UploadConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出文件上传 Excel")
    @PreAuthorize("@ss.hasPermission('system:upload:export')")
    @OperateLog(type = EXPORT)
    public void exportUploadExcel(@Valid UploadExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<UploadDO> list = uploadService.getUploadList(exportReqVO);
        // 导出 Excel
        List<UploadExcelVO> datas = UploadConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "文件上传.xls", "数据", UploadExcelVO.class, datas);
    }

    @PostMapping("/import")
    public CommonResult<String> importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        return uploadService.importReport(file);
    }

    @RequestMapping(value = "/downFile")
    public HttpServletResponse downloadFile(HttpServletResponse response,HttpServletRequest request) {

        String id = request.getParameter("id");
        UploadDO upload = uploadService.getUpload(Long.parseLong(id));
        // 要下载的文件的全路径名
        String filePath =upload.getExcelPath();
        String name = upload.getFileName();
        File file = new File(filePath);
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            //文件是否存在
            if (file.exists()) {
                //设置响应
                response.setContentType("application/octet-stream;charset=UTF-8");
                response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
                response.setHeader("Content-Disposition","attachment;filename="+name);
                response.setCharacterEncoding("UTF-8");
                os = response.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(file));
                while(bis.read(buffer) != -1){
                    os.write(buffer);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(bis != null) {
                    bis.close();
                }
                if(os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response;
    }


}
