package cn.iocoder.yudao.module.system.service.pdfread;

import cn.iocoder.yudao.module.system.dal.dataobject.upload.UploadDO;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PdfReadService {


    List<List<String>> readPdfs(List<String> pdfPaths, UploadDO upload) throws ExecutionException, InterruptedException;
}
