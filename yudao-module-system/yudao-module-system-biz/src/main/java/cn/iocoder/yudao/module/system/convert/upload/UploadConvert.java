package cn.iocoder.yudao.module.system.convert.upload;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.system.controller.admin.upload.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.upload.UploadDO;

/**
 * 文件上传 Convert
 *
 * @author 管理员
 */
@Mapper
public interface UploadConvert {

    UploadConvert INSTANCE = Mappers.getMapper(UploadConvert.class);

    UploadDO convert(UploadCreateReqVO bean);

    UploadDO convert(UploadUpdateReqVO bean);

    UploadRespVO convert(UploadDO bean);

    List<UploadRespVO> convertList(List<UploadDO> list);

    PageResult<UploadRespVO> convertPage(PageResult<UploadDO> page);

    List<UploadExcelVO> convertList02(List<UploadDO> list);

}
