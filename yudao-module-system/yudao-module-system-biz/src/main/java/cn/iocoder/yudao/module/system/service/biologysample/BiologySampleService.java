package cn.iocoder.yudao.module.system.service.biologysample;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.system.controller.admin.biologysample.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.biologysample.BiologySampleDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 生物样品入库登记 Service 接口
 *
 * @author 刘飞龙
 */
public interface BiologySampleService {

    /**
     * 创建生物样品入库登记
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBiologySample(@Valid BiologySampleCreateReqVO createReqVO);

    /**
     * 更新生物样品入库登记
     *
     * @param updateReqVO 更新信息
     */
    void updateBiologySample(@Valid BiologySampleUpdateReqVO updateReqVO);

    /**
     * 删除生物样品入库登记
     *
     * @param id 编号
     */
    void deleteBiologySample(Long id);

    /**
     * 获得生物样品入库登记
     *
     * @param id 编号
     * @return 生物样品入库登记
     */
    BiologySampleDO getBiologySample(Long id);

    /**
     * 获得生物样品入库登记列表
     *
     * @param ids 编号
     * @return 生物样品入库登记列表
     */
    List<BiologySampleDO> getBiologySampleList(Collection<Long> ids);

    /**
     * 获得生物样品入库登记分页
     *
     * @param pageReqVO 分页查询
     * @return 生物样品入库登记分页
     */
    PageResult<BiologySampleDO> getBiologySamplePage(BiologySamplePageReqVO pageReqVO);

    /**
     * 获得生物样品入库登记列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 生物样品入库登记列表
     */
    List<BiologySampleDO> getBiologySampleList(BiologySampleExportReqVO exportReqVO);

}
