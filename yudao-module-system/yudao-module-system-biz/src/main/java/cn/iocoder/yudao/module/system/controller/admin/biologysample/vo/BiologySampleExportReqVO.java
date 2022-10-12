package cn.iocoder.yudao.module.system.controller.admin.biologysample.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@ApiModel(value = "管理后台 - 生物样品入库登记 Excel 导出 Request VO", description = "参数和 BiologySamplePageReqVO 是一致的")
@Data
public class BiologySampleExportReqVO {

    @ApiModelProperty(value = "体检号")
    private String checkupNo;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "X线号")
    private String xNo;

    @ApiModelProperty(value = "血常规样本号")
    private String bloodNo;

    @ApiModelProperty(value = "生化样本号")
    private String biochemistryNo;

    @ApiModelProperty(value = "尿常规样本号")
    private String urineNo;

    @ApiModelProperty(value = "血清样本位置")
    private String serumLocation;

    @ApiModelProperty(value = "血浆样本位置")
    private String plasmaLocation;

    @ApiModelProperty(value = "血细胞样本位置")
    private String bloodCellsLocation;

    @ApiModelProperty(value = "尿样样本位置")
    private String urineLocation;

    @ApiModelProperty(value = "体检日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] checkupTime;

    @ApiModelProperty(value = "登记日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] registerTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] createTime;

}
