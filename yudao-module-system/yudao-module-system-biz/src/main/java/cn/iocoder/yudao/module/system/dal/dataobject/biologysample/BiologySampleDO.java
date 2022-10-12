package cn.iocoder.yudao.module.system.dal.dataobject.biologysample;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 生物样品入库登记 DO
 *
 * @author 刘飞龙
 */
@TableName("system_biology_sample")
@KeySequence("system_biology_sample_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiologySampleDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 体检号
     */
    private String checkupNo;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     *
     * 枚举 {@link TODO system_user_sex 对应的类}
     */
    private String sex;
    /**
     * X线号
     */
    private String xNo;
    /**
     * 血常规样本号
     */
    private String bloodNo;
    /**
     * 生化样本号
     */
    private String biochemistryNo;
    /**
     * 尿常规样本号
     */
    private String urineNo;
    /**
     * 血清样本位置
     */
    private String serumLocation;
    /**
     * 血浆样本位置
     */
    private String plasmaLocation;
    /**
     * 血细胞样本位置
     */
    private String bloodCellsLocation;
    /**
     * 尿样样本位置
     */
    private String urineLocation;
    /**
     * 体检日期
     */
    private Date checkupTime;
    /**
     * 登记日期
     */
    private Date registerTime;
    /**
     * 备注
     */
    private String remark;

}
