package cn.iocoder.yudao.module.system.controller.admin.upload;

import cn.iocoder.yudao.module.system.dal.dataobject.upload.UploadDO;
import cn.iocoder.yudao.module.system.dal.mysql.upload.UploadMapper;
import cn.iocoder.yudao.module.system.service.pdfread.PdfReadService;
import cn.iocoder.yudao.module.system.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    @Autowired
    private PdfReadService pdfReadService;

    private final String UPLOADPATH_LINUX = "/data/sxmu/uploadFile/";
    private final String UPLOADPATH_WINDOWS = "d:/sxmu/uploadFile/";

    @Async
    public void parsePdf(UploadDO upload){
        log.info("开始异步解析PDF...");
        //0:未解析;1:解析中;2:解析失败;3:解析成功;
        //修改解析状态为解析中
        upload.setAnalysisStatus(1);
        uploadMapper.updateById(upload);
        String dirPath = getDirPath() + upload.getId() + "/";
        File dirFile = new File(dirPath);
        String[] subFiles = dirFile.list();
        //解析
        try {
            log.info("开始解析...");
            Thread.sleep(10000);
            List<String> pdfPaths = new ArrayList<>();
            for (int i = 0; i < subFiles.length; i++) {
                String subFile = subFiles[i];
                if (StringUtils.endsWithIgnoreCase(subFile, ".pdf")) {
                    String pdfPath = dirPath + subFile;
                    pdfPaths.add(pdfPath);
                }
            }
            String excelPath = dirPath + upload.getFileName().substring(0,upload.getFileName().indexOf(".")) + ".xlsx";
            List<List<String>> excelCont = pdfReadService.readPdfs(pdfPaths,upload);
            ExcelUtil.writeExcel(excelCont, excelPath);
            upload.setExcelPath(excelPath);
            upload.setAnalysisStatus(3);
            upload.setAnalysisSpeed("100%");
            uploadMapper.updateById(upload);
            log.info("结束解析...");
        } catch (Exception e) {
            e.printStackTrace();
            upload.setFailReason(e.getMessage());
            upload.setAnalysisStatus(2);
            uploadMapper.updateById(upload);
            return;
        } finally {
            try {
                for (String filePath : subFiles) {
                    if (!StringUtils.endsWithIgnoreCase(filePath, ".xlsx")
                            && !StringUtils.endsWithIgnoreCase(filePath, ".xls")) {
                        Files.delete(Paths.get(dirPath + "/" + filePath));
                    }
                }
            } catch (Exception e) {
                log.error("删除文件报错", e);
            }
        }
        log.info("结束异步解析PDF...");
    }

    private String getDirPath() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return UPLOADPATH_WINDOWS;
        }
        return UPLOADPATH_LINUX;
    }
}
