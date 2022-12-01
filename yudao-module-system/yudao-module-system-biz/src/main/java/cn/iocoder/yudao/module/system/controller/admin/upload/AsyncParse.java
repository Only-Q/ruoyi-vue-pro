package cn.iocoder.yudao.module.system.controller.admin.upload;

import cn.iocoder.yudao.module.system.dal.dataobject.upload.UploadDO;
import cn.iocoder.yudao.module.system.dal.mysql.upload.UploadMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description: 异步解析PDF
 * @Author liufeilong
 * @Date 2022-11-30
 **/
@Component
@Slf4j
public class AsyncParse {

    @Resource
    private UploadMapper uploadMapper;

    private final String UPLOADPATH_LINUX = "/data/sxmu/uploadFile/";
    private final String UPLOADPATH_WINDOWS = "d:/sxmu/uploadFile/";

    @Async
    public void parsePdf(UploadDO upload){
        String path = "";
        log.info("开始异步解析PDF...");
        //0:未解析;1:解析中;2:解析失败;3:解析成功;
        //修改解析状态为解析中
        upload.setAnalysisStatus(1);
        uploadMapper.updateById(upload);
        //解析
        try {
            log.info("开始解析...");
            Thread.sleep(10000);
            log.info("结束解析...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //解析完一个修改解析进度
        upload.setAnalysisSpeed("?%");
        uploadMapper.updateById(upload);
        //全部解析完修改解析状态为解析成功，解析进度为100%，填充Excel路径，将Excel放到UPLOADPATH_XXX/upload.getId()下，名字和原文件名字一样
        upload.setAnalysisStatus(3);
        upload.setAnalysisSpeed("100%");
        upload.setExcelPath("???");
        uploadMapper.updateById(upload);
        log.info("结束异步解析PDF...");
    }
}
