package cn.iocoder.yudao.module.system.dal.mysql.upload;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.upload.UploadDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.system.controller.admin.upload.vo.*;

/**
 * 文件上传 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface UploadMapper extends BaseMapperX<UploadDO> {

    default PageResult<UploadDO> selectPage(UploadPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UploadDO>()
                .likeIfPresent(UploadDO::getFileName, reqVO.getFileName())
                .eqIfPresent(UploadDO::getAnalysisStatus, reqVO.getAnalysisStatus())
                .eqIfPresent(UploadDO::getFailReason, reqVO.getFailReason())
                .eqIfPresent(UploadDO::getExcelPath, reqVO.getExcelPath())
                .betweenIfPresent(UploadDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(UploadDO::getAnalysisSpeed, reqVO.getAnalysisSpeed())
                .orderByDesc(UploadDO::getId));
    }

    default List<UploadDO> selectList(UploadExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<UploadDO>()
                .likeIfPresent(UploadDO::getFileName, reqVO.getFileName())
                .eqIfPresent(UploadDO::getAnalysisStatus, reqVO.getAnalysisStatus())
                .eqIfPresent(UploadDO::getFailReason, reqVO.getFailReason())
                .eqIfPresent(UploadDO::getExcelPath, reqVO.getExcelPath())
                .betweenIfPresent(UploadDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(UploadDO::getAnalysisSpeed, reqVO.getAnalysisSpeed())
                .orderByDesc(UploadDO::getId));
    }

}
