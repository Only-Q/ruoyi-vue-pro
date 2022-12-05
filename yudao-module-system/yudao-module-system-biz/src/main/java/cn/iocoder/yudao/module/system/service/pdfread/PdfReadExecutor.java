package cn.iocoder.yudao.module.system.service.pdfread;

import cn.iocoder.yudao.module.system.util.PdfOcrUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.concurrent.Future;
@Slf4j
@Service("pdfReadExecutor")
public class PdfReadExecutor {
    @Async("threadPoolTaskExecutor")
    public Future<Object> readPdf(String pdfPath) {
        try {
            log.info("pdf解析" + pdfPath);
            LinkedHashMap<String, Object> pdfRes = PdfOcrUtils.processPdfContent(pdfPath);
            return new AsyncResult<>(pdfRes);
        } catch (Exception e) {
            log.error("pdf解析报错", e);
            return new AsyncResult<>(pdfPath + "\t" + e.getMessage());
        }
    }

}
