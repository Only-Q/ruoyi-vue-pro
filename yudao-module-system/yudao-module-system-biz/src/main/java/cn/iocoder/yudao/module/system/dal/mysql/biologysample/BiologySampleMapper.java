package cn.iocoder.yudao.module.system.dal.mysql.biologysample;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.biologysample.BiologySampleDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.system.controller.admin.biologysample.vo.*;

/**
 * 生物样品入库登记 Mapper
 *
 * @author 刘飞龙
 */
@Mapper
public interface BiologySampleMapper extends BaseMapperX<BiologySampleDO> {

    default PageResult<BiologySampleDO> selectPage(BiologySamplePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BiologySampleDO>()
                .eqIfPresent(BiologySampleDO::getCheckupNo, reqVO.getCheckupNo())
                .likeIfPresent(BiologySampleDO::getName, reqVO.getName())
                .eqIfPresent(BiologySampleDO::getSex, reqVO.getSex())
                .eqIfPresent(BiologySampleDO::getXNo, reqVO.getXNo())
                .eqIfPresent(BiologySampleDO::getBloodNo, reqVO.getBloodNo())
                .eqIfPresent(BiologySampleDO::getBiochemistryNo, reqVO.getBiochemistryNo())
                .eqIfPresent(BiologySampleDO::getUrineNo, reqVO.getUrineNo())
                .eqIfPresent(BiologySampleDO::getSerumLocation, reqVO.getSerumLocation())
                .eqIfPresent(BiologySampleDO::getPlasmaLocation, reqVO.getPlasmaLocation())
                .eqIfPresent(BiologySampleDO::getBloodCellsLocation, reqVO.getBloodCellsLocation())
                .eqIfPresent(BiologySampleDO::getUrineLocation, reqVO.getUrineLocation())
                .betweenIfPresent(BiologySampleDO::getCheckupTime, reqVO.getCheckupTime())
                .betweenIfPresent(BiologySampleDO::getRegisterTime, reqVO.getRegisterTime())
                .eqIfPresent(BiologySampleDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(BiologySampleDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BiologySampleDO::getId));
    }

    default List<BiologySampleDO> selectList(BiologySampleExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<BiologySampleDO>()
                .eqIfPresent(BiologySampleDO::getCheckupNo, reqVO.getCheckupNo())
                .likeIfPresent(BiologySampleDO::getName, reqVO.getName())
                .eqIfPresent(BiologySampleDO::getSex, reqVO.getSex())
                .eqIfPresent(BiologySampleDO::getXNo, reqVO.getXNo())
                .eqIfPresent(BiologySampleDO::getBloodNo, reqVO.getBloodNo())
                .eqIfPresent(BiologySampleDO::getBiochemistryNo, reqVO.getBiochemistryNo())
                .eqIfPresent(BiologySampleDO::getUrineNo, reqVO.getUrineNo())
                .eqIfPresent(BiologySampleDO::getSerumLocation, reqVO.getSerumLocation())
                .eqIfPresent(BiologySampleDO::getPlasmaLocation, reqVO.getPlasmaLocation())
                .eqIfPresent(BiologySampleDO::getBloodCellsLocation, reqVO.getBloodCellsLocation())
                .eqIfPresent(BiologySampleDO::getUrineLocation, reqVO.getUrineLocation())
                .betweenIfPresent(BiologySampleDO::getCheckupTime, reqVO.getCheckupTime())
                .betweenIfPresent(BiologySampleDO::getRegisterTime, reqVO.getRegisterTime())
                .eqIfPresent(BiologySampleDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(BiologySampleDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BiologySampleDO::getId));
    }

    List<BiologySampleDO> getSampleInfo(String sampleNo);

}
