package cn.iocoder.yudao.module.system.controller.admin.biologysample.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description: 样本信息导入实体类
 * @Author liufeilong
 * @Date 2022-10-13
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false) // 设置 chain = false，避免用户导入有问题
public class SampleImportExcelVo {

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty(value = "性别", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.USER_SEX)
    private Integer sex;

    @ExcelProperty("体检编号")
    private String checkupNo;

    @ExcelProperty("血常规样本号")
    private String bloodNo;

    @ExcelProperty("生化样本号")
    private String biochemistryNo;

    @ExcelProperty("尿常规样本号")
    private String urineNo;

    @ExcelProperty("摄片号")
    @JsonProperty(value = "xNo")
    private String xNo;

    @ExcelProperty("登记时间")
    private String registerTime;
}
