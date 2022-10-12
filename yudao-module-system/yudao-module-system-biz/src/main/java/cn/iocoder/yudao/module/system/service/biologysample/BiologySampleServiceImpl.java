package cn.iocoder.yudao.module.system.service.biologysample;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.system.controller.admin.biologysample.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.biologysample.BiologySampleDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.system.convert.biologysample.BiologySampleConvert;
import cn.iocoder.yudao.module.system.dal.mysql.biologysample.BiologySampleMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * 生物样品入库登记 Service 实现类
 *
 * @author 刘飞龙
 */
@Service
@Validated
public class BiologySampleServiceImpl implements BiologySampleService {

    @Resource
    private BiologySampleMapper biologySampleMapper;

    @Override
    public Long createBiologySample(BiologySampleCreateReqVO createReqVO) {
        // 插入
        BiologySampleDO biologySample = BiologySampleConvert.INSTANCE.convert(createReqVO);
        biologySampleMapper.insert(biologySample);
        // 返回
        return biologySample.getId();
    }

    @Override
    public void updateBiologySample(BiologySampleUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateBiologySampleExists(updateReqVO.getId());
        // 更新
        BiologySampleDO updateObj = BiologySampleConvert.INSTANCE.convert(updateReqVO);
        biologySampleMapper.updateById(updateObj);
    }

    @Override
    public void deleteBiologySample(Long id) {
        // 校验存在
        this.validateBiologySampleExists(id);
        // 删除
        biologySampleMapper.deleteById(id);
    }

    private void validateBiologySampleExists(Long id) {
        if (biologySampleMapper.selectById(id) == null) {
            throw exception(BIOLOGY_SAMPLE_NOT_EXISTS);
        }
    }

    @Override
    public BiologySampleDO getBiologySample(Long id) {
        return biologySampleMapper.selectById(id);
    }

    @Override
    public List<BiologySampleDO> getBiologySampleList(Collection<Long> ids) {
        return biologySampleMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<BiologySampleDO> getBiologySamplePage(BiologySamplePageReqVO pageReqVO) {
        return biologySampleMapper.selectPage(pageReqVO);
    }

    @Override
    public List<BiologySampleDO> getBiologySampleList(BiologySampleExportReqVO exportReqVO) {
        return biologySampleMapper.selectList(exportReqVO);
    }

}
