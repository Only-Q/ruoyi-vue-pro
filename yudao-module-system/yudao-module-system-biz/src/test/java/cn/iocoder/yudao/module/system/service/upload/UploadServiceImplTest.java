package cn.iocoder.yudao.module.system.service.upload;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.system.controller.admin.upload.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.upload.UploadDO;
import cn.iocoder.yudao.module.system.dal.mysql.upload.UploadMapper;
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
* {@link UploadServiceImpl} 的单元测试类
*
* @author 管理员
*/
@Import(UploadServiceImpl.class)
public class UploadServiceImplTest extends BaseDbUnitTest {

    @Resource
    private UploadServiceImpl uploadService;

    @Resource
    private UploadMapper uploadMapper;

    @Test
    public void testCreateUpload_success() {
        // 准备参数
        UploadCreateReqVO reqVO = randomPojo(UploadCreateReqVO.class);

        // 调用
        Long uploadId = uploadService.createUpload(reqVO);
        // 断言
        assertNotNull(uploadId);
        // 校验记录的属性是否正确
        UploadDO upload = uploadMapper.selectById(uploadId);
        assertPojoEquals(reqVO, upload);
    }

    @Test
    public void testUpdateUpload_success() {
        // mock 数据
        UploadDO dbUpload = randomPojo(UploadDO.class);
        uploadMapper.insert(dbUpload);// @Sql: 先插入出一条存在的数据
        // 准备参数
        UploadUpdateReqVO reqVO = randomPojo(UploadUpdateReqVO.class, o -> {
            o.setId(dbUpload.getId()); // 设置更新的 ID
        });

        // 调用
        uploadService.updateUpload(reqVO);
        // 校验是否更新正确
        UploadDO upload = uploadMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, upload);
    }

    @Test
    public void testUpdateUpload_notExists() {
        // 准备参数
        UploadUpdateReqVO reqVO = randomPojo(UploadUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> uploadService.updateUpload(reqVO), UPLOAD_NOT_EXISTS);
    }

    @Test
    public void testDeleteUpload_success() {
        // mock 数据
        UploadDO dbUpload = randomPojo(UploadDO.class);
        uploadMapper.insert(dbUpload);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbUpload.getId();

        // 调用
        uploadService.deleteUpload(id);
       // 校验数据不存在了
       assertNull(uploadMapper.selectById(id));
    }

    @Test
    public void testDeleteUpload_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> uploadService.deleteUpload(id), UPLOAD_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetUploadPage() {
       // mock 数据
       UploadDO dbUpload = randomPojo(UploadDO.class, o -> { // 等会查询到
           o.setFileName(null);
           o.setAnalysisStatus(null);
           o.setFailReason(null);
           o.setExcelPath(null);
           o.setCreateTime(null);
           o.setAnalysisSpeed(null);
       });
       uploadMapper.insert(dbUpload);
       // 测试 fileName 不匹配
       uploadMapper.insert(cloneIgnoreId(dbUpload, o -> o.setFileName(null)));
       // 测试 analysisStatus 不匹配
       uploadMapper.insert(cloneIgnoreId(dbUpload, o -> o.setAnalysisStatus(null)));
       // 测试 failReason 不匹配
       uploadMapper.insert(cloneIgnoreId(dbUpload, o -> o.setFailReason(null)));
       // 测试 excelPath 不匹配
       uploadMapper.insert(cloneIgnoreId(dbUpload, o -> o.setExcelPath(null)));
       // 测试 createTime 不匹配
       uploadMapper.insert(cloneIgnoreId(dbUpload, o -> o.setCreateTime(null)));
       // 测试 analysisSpeed 不匹配
       uploadMapper.insert(cloneIgnoreId(dbUpload, o -> o.setAnalysisSpeed(null)));
       // 准备参数
       UploadPageReqVO reqVO = new UploadPageReqVO();
       reqVO.setFileName(null);
       reqVO.setAnalysisStatus(null);
       reqVO.setFailReason(null);
       reqVO.setExcelPath(null);
       reqVO.setCreateTime((new Date[]{}));
       reqVO.setAnalysisSpeed(null);

       // 调用
       PageResult<UploadDO> pageResult = uploadService.getUploadPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbUpload, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetUploadList() {
       // mock 数据
       UploadDO dbUpload = randomPojo(UploadDO.class, o -> { // 等会查询到
           o.setFileName(null);
           o.setAnalysisStatus(null);
           o.setFailReason(null);
           o.setExcelPath(null);
           o.setCreateTime(null);
           o.setAnalysisSpeed(null);
       });
       uploadMapper.insert(dbUpload);
       // 测试 fileName 不匹配
       uploadMapper.insert(cloneIgnoreId(dbUpload, o -> o.setFileName(null)));
       // 测试 analysisStatus 不匹配
       uploadMapper.insert(cloneIgnoreId(dbUpload, o -> o.setAnalysisStatus(null)));
       // 测试 failReason 不匹配
       uploadMapper.insert(cloneIgnoreId(dbUpload, o -> o.setFailReason(null)));
       // 测试 excelPath 不匹配
       uploadMapper.insert(cloneIgnoreId(dbUpload, o -> o.setExcelPath(null)));
       // 测试 createTime 不匹配
       uploadMapper.insert(cloneIgnoreId(dbUpload, o -> o.setCreateTime(null)));
       // 测试 analysisSpeed 不匹配
       uploadMapper.insert(cloneIgnoreId(dbUpload, o -> o.setAnalysisSpeed(null)));
       // 准备参数
       UploadExportReqVO reqVO = new UploadExportReqVO();
       reqVO.setFileName(null);
       reqVO.setAnalysisStatus(null);
       reqVO.setFailReason(null);
       reqVO.setExcelPath(null);
       reqVO.setCreateTime((new Date[]{}));
       reqVO.setAnalysisSpeed(null);

       // 调用
       List<UploadDO> list = uploadService.getUploadList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbUpload, list.get(0));
    }

}
