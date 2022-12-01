package cn.iocoder.yudao.module.system.dal.dataobject.upload;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 文件上传 DO
 *
 * @author 管理员
 */
@TableName("system_upload")
@KeySequence("system_upload_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 解析状态(0:未解析;1:解析中;2:解析失败;3:解析成功;)
     *
     * 枚举 {@link TODO analysis_status 对应的类}
     */
    private Integer analysisStatus;
    /**
     * 解析失败原因
     */
    private String failReason;
    /**
     * Excel路径
     */
    private String excelPath;
    /**
     * 解析进度
     */
    private String analysisSpeed;

}
