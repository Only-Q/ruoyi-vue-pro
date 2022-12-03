package cn.iocoder.yudao.module.system.util;

import cn.hutool.poi.excel.ExcelWriter;

import java.util.List;

public class ExcelUtil {

    public static void writeExcel(List<List<String>> contents, String excelPath) {
        ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter(excelPath);
        writer.write(contents);
        writer.close();
    }

}
