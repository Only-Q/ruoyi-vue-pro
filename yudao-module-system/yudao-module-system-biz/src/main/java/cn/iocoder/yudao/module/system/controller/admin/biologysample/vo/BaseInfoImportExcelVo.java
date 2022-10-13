package cn.iocoder.yudao.module.system.controller.admin.biologysample.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description: 基础信息导入实体类
 * @Author liufeilong
 * @Date 2022-10-13
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false) // 设置 chain = false，避免用户导入有问题
public class BaseInfoImportExcelVo {
    @ExcelProperty("日期")
    private String checkupTime;

    @ExcelProperty("体检号")
    private String checkupNo;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty(value = "性别", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.USER_SEX)
    private Integer sex;

    @ExcelProperty("X线号")
    @JsonProperty(value = "xNo")
    private String xNo;
}
