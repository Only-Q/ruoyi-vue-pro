package cn.iocoder.yudao.module.system.controller.admin.upload.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

/**
* 文件上传 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class UploadBaseVO {

    @ApiModelProperty(value = "文件名", required = true)
    @NotNull(message = "文件名不能为空")
    private String fileName;

    @ApiModelProperty(value = "解析状态(0:未解析;1:解析中;2:解析失败;3:解析成功;)", required = true)
    @NotNull(message = "解析状态(0:未解析;1:解析中;2:解析失败;3:解析成功;)不能为空")
    private Integer analysisStatus;

    @ApiModelProperty(value = "解析失败原因")
    private String failReason;

    @ApiModelProperty(value = "Excel路径")
    private String excelPath;

    @ApiModelProperty(value = "解析进度")
    private String analysisSpeed;

}
