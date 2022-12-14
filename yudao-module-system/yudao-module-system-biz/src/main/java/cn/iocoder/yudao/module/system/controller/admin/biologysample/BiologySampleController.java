package cn.iocoder.yudao.module.system.controller.admin.biologysample;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.*;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.system.controller.admin.biologysample.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.biologysample.BiologySampleDO;
import cn.iocoder.yudao.module.system.convert.biologysample.BiologySampleConvert;
import cn.iocoder.yudao.module.system.service.biologysample.BiologySampleService;

@Api(tags = "管理后台 - 生物样品入库登记")
@RestController
@RequestMapping("/system/biology-sample")
@Validated
public class BiologySampleController {

    @Resource
    private BiologySampleService biologySampleService;

    @PostMapping("/create")
    @ApiOperation("创建生物样品入库登记")
    @PreAuthorize("@ss.hasPermission('system:biology-sample:create')")
    public CommonResult<Long> createBiologySample(@Valid @RequestBody BiologySampleCreateReqVO createReqVO) {
        return success(biologySampleService.createBiologySample(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新生物样品入库登记")
    @PreAuthorize("@ss.hasPermission('system:biology-sample:update')")
    public CommonResult<Boolean> updateBiologySample(@Valid @RequestBody BiologySampleUpdateReqVO updateReqVO) {
        biologySampleService.updateBiologySample(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除生物样品入库登记")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('system:biology-sample:delete')")
    public CommonResult<Boolean> deleteBiologySample(@RequestParam("id") Long id) {
        biologySampleService.deleteBiologySample(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得生物样品入库登记")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('system:biology-sample:query')")
    public CommonResult<BiologySampleRespVO> getBiologySample(@RequestParam("id") Long id) {
        BiologySampleDO biologySample = biologySampleService.getBiologySample(id);
        return success(BiologySampleConvert.INSTANCE.convert(biologySample));
    }

    @GetMapping("/list")
    @ApiOperation("获得生物样品入库登记列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('system:biology-sample:query')")
    public CommonResult<List<BiologySampleRespVO>> getBiologySampleList(@RequestParam("ids") Collection<Long> ids) {
        List<BiologySampleDO> list = biologySampleService.getBiologySampleList(ids);
        return success(BiologySampleConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得生物样品入库登记分页")
    @PreAuthorize("@ss.hasPermission('system:biology-sample:query')")
    public CommonResult<PageResult<BiologySampleRespVO>> getBiologySamplePage(@Valid BiologySamplePageReqVO pageVO) {
        PageResult<BiologySampleDO> pageResult = biologySampleService.getBiologySamplePage(pageVO);
        return success(BiologySampleConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出生物样品入库登记 Excel")
    @PreAuthorize("@ss.hasPermission('system:biology-sample:export')")
    @OperateLog(type = EXPORT)
    public void exportBiologySampleExcel(@Valid BiologySampleExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<BiologySampleDO> list = biologySampleService.getBiologySampleList(exportReqVO);
        // 导出 Excel
        List<BiologySampleExcelVO> datas = BiologySampleConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "生物样品入库登记.xls", "数据", BiologySampleExcelVO.class, datas);
    }

}
