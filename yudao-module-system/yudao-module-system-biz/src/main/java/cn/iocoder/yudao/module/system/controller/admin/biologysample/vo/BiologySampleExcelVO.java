package cn.iocoder.yudao.module.system.controller.admin.biologysample.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;


/**
 * 生物样品入库登记 Excel VO
 *
 * @author 刘飞龙
 */
@Data
public class BiologySampleExcelVO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("体检号")
    private String checkupNo;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty(value = "性别", converter = DictConvert.class)
    @DictFormat("system_user_sex") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private String sex;

    @ExcelProperty("X线号")
    private String xNo;

    @ExcelProperty("血常规样本号")
    private String bloodNo;

    @ExcelProperty("生化样本号")
    private String biochemistryNo;

    @ExcelProperty("尿常规样本号")
    private String urineNo;

    @ExcelProperty("血清样本位置")
    private String serumLocation;

    @ExcelProperty("血浆样本位置")
    private String plasmaLocation;

    @ExcelProperty("血细胞样本位置")
    private String bloodCellsLocation;

    @ExcelProperty("尿样样本位置")
    private String urineLocation;

    @ExcelProperty("体检日期")
    private Date checkupTime;

    @ExcelProperty("登记日期")
    private Date registerTime;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建时间")
    private Date createTime;

}
