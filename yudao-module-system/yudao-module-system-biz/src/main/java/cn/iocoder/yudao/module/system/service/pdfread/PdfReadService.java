package cn.iocoder.yudao.module.system.service.pdfread;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PdfReadService {


    List<List<String>> readPdfs(List<String> pdfPaths) throws ExecutionException, InterruptedException;
}
