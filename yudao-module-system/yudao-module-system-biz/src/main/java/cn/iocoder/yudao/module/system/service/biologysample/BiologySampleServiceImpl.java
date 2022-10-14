package cn.iocoder.yudao.module.system.service.biologysample;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Override
    public List<BiologySampleDO> getSampleInfoBySampleNo(String sampleNo) {
        return biologySampleMapper.getSampleInfoBySampleNo(sampleNo);
    }

    @Override
    public List<BiologySampleDO> getSampleInfoByLocationNo(String locationNo) {
        return biologySampleMapper.getSampleInfoByLocationNo(locationNo);
    }

    @Override
    @Transactional
    public CommonResult<String> importBaseInfo(List<BaseInfoImportExcelVo> list) {
        list.stream().forEach(f -> {
            BiologySampleExportReqVO en = new BiologySampleExportReqVO();
            en.setCheckupNo(f.getCheckupNo());
            List<BiologySampleDO> biologySampleList = getBiologySampleList(en);
            if(biologySampleList.size() == 0){
                // 插入
                BiologySampleCreateReqVO createReqVO = new BiologySampleCreateReqVO();
                createReqVO.setCheckupNo(f.getCheckupNo());
                createReqVO.setName(f.getName());
                createReqVO.setSex(f.getSex()+"");
                createReqVO.setXNo(f.getXNo());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                try {
                    createReqVO.setCheckupTime(sdf.parse(f.getCheckupTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                BiologySampleDO biologySample = BiologySampleConvert.INSTANCE.convert(createReqVO);
                biologySampleMapper.insert(biologySample);
            }
        });
        return CommonResult.success("基础信息导入成功");
    }

    @Override
    @Transactional
    public CommonResult<String> importSample(List<SampleImportExcelVo> list) {
        list.stream().forEach(f -> {
            BiologySampleExportReqVO en = new BiologySampleExportReqVO();
            en.setCheckupNo(f.getCheckupNo());
            List<BiologySampleDO> biologySampleList = getBiologySampleList(en);
            if(biologySampleList.size()>0){
                BiologySampleDO biologySampleDO = biologySampleList.get(0);
                biologySampleDO.setBloodNo(f.getBloodNo());
                biologySampleDO.setBiochemistryNo(f.getBiochemistryNo());
                biologySampleDO.setUrineNo(f.getUrineNo());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                try {
                    biologySampleDO.setRegisterTime(sdf.parse(f.getRegisterTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // 更新
                biologySampleMapper.updateById(biologySampleDO);
            }
        });
        return CommonResult.success("样本号导入成功");
    }

}
