package cn.iocoder.yudao.module.system.service.pdfread;

import cn.iocoder.yudao.module.system.util.PdfOcrUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.concurrent.Future;

@Service("pdfReadExecutor")
public class PdfReadExecutor {
    @Async
    public Future<LinkedHashMap<String,Object>> readPdf(String pdfPath) {
        try {
            LinkedHashMap<String, Object> pdfRes = PdfOcrUtils.processPdfContent(pdfPath);
            return new AsyncResult<>(pdfRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(null);
    }

}
