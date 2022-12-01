package cn.iocoder.yudao.module.system.service.upload;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.system.controller.admin.upload.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.upload.UploadDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传 Service 接口
 *
 * @author 管理员
 */
public interface UploadService {

    /**
     * 创建文件上传
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createUpload(@Valid UploadCreateReqVO createReqVO);

    /**
     * 更新文件上传
     *
     * @param updateReqVO 更新信息
     */
    void updateUpload(@Valid UploadUpdateReqVO updateReqVO);

    /**
     * 删除文件上传
     *
     * @param id 编号
     */
    void deleteUpload(Long id);

    /**
     * 获得文件上传
     *
     * @param id 编号
     * @return 文件上传
     */
    UploadDO getUpload(Long id);

    /**
     * 获得文件上传列表
     *
     * @param ids 编号
     * @return 文件上传列表
     */
    List<UploadDO> getUploadList(Collection<Long> ids);

    /**
     * 获得文件上传分页
     *
     * @param pageReqVO 分页查询
     * @return 文件上传分页
     */
    PageResult<UploadDO> getUploadPage(UploadPageReqVO pageReqVO);

    /**
     * 获得文件上传列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 文件上传列表
     */
    List<UploadDO> getUploadList(UploadExportReqVO exportReqVO);

    CommonResult<String> importReport(MultipartFile file);

}
