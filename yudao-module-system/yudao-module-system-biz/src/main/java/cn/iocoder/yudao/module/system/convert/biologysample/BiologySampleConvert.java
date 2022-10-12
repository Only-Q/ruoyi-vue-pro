package cn.iocoder.yudao.module.system.convert.biologysample;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.system.controller.admin.biologysample.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.biologysample.BiologySampleDO;

/**
 * 生物样品入库登记 Convert
 *
 * @author 刘飞龙
 */
@Mapper
public interface BiologySampleConvert {

    BiologySampleConvert INSTANCE = Mappers.getMapper(BiologySampleConvert.class);

    BiologySampleDO convert(BiologySampleCreateReqVO bean);

    BiologySampleDO convert(BiologySampleUpdateReqVO bean);

    BiologySampleRespVO convert(BiologySampleDO bean);

    List<BiologySampleRespVO> convertList(List<BiologySampleDO> list);

    PageResult<BiologySampleRespVO> convertPage(PageResult<BiologySampleDO> page);

    List<BiologySampleExcelVO> convertList02(List<BiologySampleDO> list);

}
