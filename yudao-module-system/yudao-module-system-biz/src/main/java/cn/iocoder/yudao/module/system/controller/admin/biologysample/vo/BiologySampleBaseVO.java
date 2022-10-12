package cn.iocoder.yudao.module.system.controller.admin.biologysample.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
* 生物样品入库登记 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class BiologySampleBaseVO {

    @ApiModelProperty(value = "体检号", required = true)
    @NotNull(message = "体检号不能为空")
    private String checkupNo;

    @ApiModelProperty(value = "姓名", required = true)
    @NotNull(message = "姓名不能为空")
    private String name;

    @ApiModelProperty(value = "性别", required = true)
    @NotNull(message = "性别不能为空")
    private String sex;

    @ApiModelProperty(value = "X线号", required = true)
    @NotNull(message = "X线号不能为空")
    @JsonProperty(value = "xNo")
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

    @ApiModelProperty(value = "体检日期", required = true)
    @NotNull(message = "体检日期不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date checkupTime;

    @ApiModelProperty(value = "登记日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date registerTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}
