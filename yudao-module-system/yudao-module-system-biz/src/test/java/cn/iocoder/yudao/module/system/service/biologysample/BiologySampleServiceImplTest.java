package cn.iocoder.yudao.module.system.service.biologysample;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.system.controller.admin.biologysample.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.biologysample.BiologySampleDO;
import cn.iocoder.yudao.module.system.dal.mysql.biologysample.BiologySampleMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
* {@link BiologySampleServiceImpl} 的单元测试类
*
* @author 刘飞龙
*/
@Import(BiologySampleServiceImpl.class)
public class BiologySampleServiceImplTest extends BaseDbUnitTest {

    @Resource
    private BiologySampleServiceImpl biologySampleService;

    @Resource
    private BiologySampleMapper biologySampleMapper;

    @Test
    public void testCreateBiologySample_success() {
        // 准备参数
        BiologySampleCreateReqVO reqVO = randomPojo(BiologySampleCreateReqVO.class);

        // 调用
        Long biologySampleId = biologySampleService.createBiologySample(reqVO);
        // 断言
        assertNotNull(biologySampleId);
        // 校验记录的属性是否正确
        BiologySampleDO biologySample = biologySampleMapper.selectById(biologySampleId);
        assertPojoEquals(reqVO, biologySample);
    }

    @Test
    public void testUpdateBiologySample_success() {
        // mock 数据
        BiologySampleDO dbBiologySample = randomPojo(BiologySampleDO.class);
        biologySampleMapper.insert(dbBiologySample);// @Sql: 先插入出一条存在的数据
        // 准备参数
        BiologySampleUpdateReqVO reqVO = randomPojo(BiologySampleUpdateReqVO.class, o -> {
            o.setId(dbBiologySample.getId()); // 设置更新的 ID
        });

        // 调用
        biologySampleService.updateBiologySample(reqVO);
        // 校验是否更新正确
        BiologySampleDO biologySample = biologySampleMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, biologySample);
    }

    @Test
    public void testUpdateBiologySample_notExists() {
        // 准备参数
        BiologySampleUpdateReqVO reqVO = randomPojo(BiologySampleUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> biologySampleService.updateBiologySample(reqVO), BIOLOGY_SAMPLE_NOT_EXISTS);
    }

    @Test
    public void testDeleteBiologySample_success() {
        // mock 数据
        BiologySampleDO dbBiologySample = randomPojo(BiologySampleDO.class);
        biologySampleMapper.insert(dbBiologySample);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbBiologySample.getId();

        // 调用
        biologySampleService.deleteBiologySample(id);
       // 校验数据不存在了
       assertNull(biologySampleMapper.selectById(id));
    }

    @Test
    public void testDeleteBiologySample_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> biologySampleService.deleteBiologySample(id), BIOLOGY_SAMPLE_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetBiologySamplePage() {
       // mock 数据
       BiologySampleDO dbBiologySample = randomPojo(BiologySampleDO.class, o -> { // 等会查询到
           o.setCheckupNo(null);
           o.setName(null);
           o.setSex(null);
           o.setXNo(null);
           o.setBloodNo(null);
           o.setBiochemistryNo(null);
           o.setUrineNo(null);
           o.setSerumLocation(null);
           o.setPlasmaLocation(null);
           o.setBloodCellsLocation(null);
           o.setUrineLocation(null);
           o.setCheckupTime(null);
           o.setRegisterTime(null);
           o.setRemark(null);
           o.setCreateTime(null);
       });
       biologySampleMapper.insert(dbBiologySample);
       // 测试 checkupNo 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setCheckupNo(null)));
       // 测试 name 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setName(null)));
       // 测试 sex 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setSex(null)));
       // 测试 xNo 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setXNo(null)));
       // 测试 bloodNo 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setBloodNo(null)));
       // 测试 biochemistryNo 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setBiochemistryNo(null)));
       // 测试 urineNo 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setUrineNo(null)));
       // 测试 serumLocation 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setSerumLocation(null)));
       // 测试 plasmaLocation 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setPlasmaLocation(null)));
       // 测试 bloodCellsLocation 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setBloodCellsLocation(null)));
       // 测试 urineLocation 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setUrineLocation(null)));
       // 测试 checkupTime 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setCheckupTime(null)));
       // 测试 registerTime 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setRegisterTime(null)));
       // 测试 remark 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setRemark(null)));
       // 测试 createTime 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setCreateTime(null)));
       // 准备参数
       BiologySamplePageReqVO reqVO = new BiologySamplePageReqVO();
       reqVO.setCheckupNo(null);
       reqVO.setName(null);
       reqVO.setSex(null);
       reqVO.setXNo(null);
       reqVO.setBloodNo(null);
       reqVO.setBiochemistryNo(null);
       reqVO.setUrineNo(null);
       reqVO.setSerumLocation(null);
       reqVO.setPlasmaLocation(null);
       reqVO.setBloodCellsLocation(null);
       reqVO.setUrineLocation(null);
       reqVO.setCheckupTime((new Date[]{}));
       reqVO.setRegisterTime((new Date[]{}));
       reqVO.setRemark(null);
       reqVO.setCreateTime((new Date[]{}));

       // 调用
       PageResult<BiologySampleDO> pageResult = biologySampleService.getBiologySamplePage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbBiologySample, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetBiologySampleList() {
       // mock 数据
       BiologySampleDO dbBiologySample = randomPojo(BiologySampleDO.class, o -> { // 等会查询到
           o.setCheckupNo(null);
           o.setName(null);
           o.setSex(null);
           o.setXNo(null);
           o.setBloodNo(null);
           o.setBiochemistryNo(null);
           o.setUrineNo(null);
           o.setSerumLocation(null);
           o.setPlasmaLocation(null);
           o.setBloodCellsLocation(null);
           o.setUrineLocation(null);
           o.setCheckupTime(null);
           o.setRegisterTime(null);
           o.setRemark(null);
           o.setCreateTime(null);
       });
       biologySampleMapper.insert(dbBiologySample);
       // 测试 checkupNo 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setCheckupNo(null)));
       // 测试 name 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setName(null)));
       // 测试 sex 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setSex(null)));
       // 测试 xNo 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setXNo(null)));
       // 测试 bloodNo 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setBloodNo(null)));
       // 测试 biochemistryNo 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setBiochemistryNo(null)));
       // 测试 urineNo 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setUrineNo(null)));
       // 测试 serumLocation 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setSerumLocation(null)));
       // 测试 plasmaLocation 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setPlasmaLocation(null)));
       // 测试 bloodCellsLocation 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setBloodCellsLocation(null)));
       // 测试 urineLocation 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setUrineLocation(null)));
       // 测试 checkupTime 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setCheckupTime(null)));
       // 测试 registerTime 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setRegisterTime(null)));
       // 测试 remark 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setRemark(null)));
       // 测试 createTime 不匹配
       biologySampleMapper.insert(cloneIgnoreId(dbBiologySample, o -> o.setCreateTime(null)));
       // 准备参数
       BiologySampleExportReqVO reqVO = new BiologySampleExportReqVO();
       reqVO.setCheckupNo(null);
       reqVO.setName(null);
       reqVO.setSex(null);
       reqVO.setXNo(null);
       reqVO.setBloodNo(null);
       reqVO.setBiochemistryNo(null);
       reqVO.setUrineNo(null);
       reqVO.setSerumLocation(null);
       reqVO.setPlasmaLocation(null);
       reqVO.setBloodCellsLocation(null);
       reqVO.setUrineLocation(null);
       reqVO.setCheckupTime((new Date[]{}));
       reqVO.setRegisterTime((new Date[]{}));
       reqVO.setRemark(null);
       reqVO.setCreateTime((new Date[]{}));

       // 调用
       List<BiologySampleDO> list = biologySampleService.getBiologySampleList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbBiologySample, list.get(0));
    }

}
