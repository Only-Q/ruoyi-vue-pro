package cn.iocoder.yudao.module.system.controller.admin.upload.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;


/**
 * 文件上传 Excel VO
 *
 * @author 管理员
 */
@Data
public class UploadExcelVO {

    @ExcelProperty("编号")
    private Long id;

    @ExcelProperty("文件名")
    private String fileName;

    @ExcelProperty(value = "解析状态(0:未解析;1:解析中;2:解析失败;3:解析成功;)", converter = DictConvert.class)
    @DictFormat("analysis_status") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer analysisStatus;

    @ExcelProperty("解析失败原因")
    private String failReason;

    @ExcelProperty("Excel路径")
    private String excelPath;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("解析进度")
    private String analysisSpeed;

}
