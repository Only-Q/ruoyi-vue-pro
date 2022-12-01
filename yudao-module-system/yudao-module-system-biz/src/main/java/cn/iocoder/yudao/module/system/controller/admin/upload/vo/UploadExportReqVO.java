package cn.iocoder.yudao.module.system.controller.admin.upload.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@ApiModel(value = "管理后台 - 文件上传 Excel 导出 Request VO", description = "参数和 UploadPageReqVO 是一致的")
@Data
public class UploadExportReqVO {

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "解析状态(0:未解析;1:解析中;2:解析失败;3:解析成功;)")
    private Integer analysisStatus;

    @ApiModelProperty(value = "解析失败原因")
    private String failReason;

    @ApiModelProperty(value = "Excel路径")
    private String excelPath;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] createTime;

    @ApiModelProperty(value = "解析进度")
    private String analysisSpeed;

}
