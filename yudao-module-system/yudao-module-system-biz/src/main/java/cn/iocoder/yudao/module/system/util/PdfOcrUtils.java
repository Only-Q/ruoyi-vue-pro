package cn.iocoder.yudao.module.system.util;

import cn.hutool.core.convert.Convert;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.*;

public class PdfOcrUtils {

    public static LinkedHashMap<String, Object> processPdfContent(String pdfPath) throws IOException, InterruptedException {
        List<List<String>> content = readPdfByOcr(pdfPath);
        LinkedHashMap<String, Object> excelMap = new LinkedHashMap<>();
        for (List<String> pageList : content) {
            String firLine = pageList.get(0).replaceAll("[、：一二三四五六七八九十]*[:]*", "");
            if (firLine.equals("职业健康检查表说明") || firLine.equals("症状")) {
                continue;
            }
            if (firLine.equals("西山煤电(集团)有限责任公司职业病防治所")) {
                String nextTitle = null;
                for (int j = 2; j < pageList.size(); j++) {
                    String line = pageList.get(j).replaceAll("[一二三四五六七八九十 　]*", "")
                            .replaceAll("[(（]+", "(").replaceAll("[)）]+", ")");
                    if (line.contains(":") || line.contains("：")) {
                        List<String> lineSplit = Arrays.asList(line.split("[:：]"));
                        excelMap.put(getListItemByIndex(lineSplit, 0).replaceAll("[ 　:]{0,}", ""),
                                getListItemByIndex(lineSplit, 1));
                        nextTitle = getListItemByIndex(lineSplit, 0);
                    } else {
                        String lastVal = Convert.toStr(excelMap.get(nextTitle));
                        excelMap.put(nextTitle, lastVal + line);
                    }
                }
            } else if ("职业健康检查报告".equals(firLine)) {
                List<String> fields = Arrays.asList("婚姻状况", "总工龄", "接害工龄");
                for (int i = 0; i < pageList.size(); i++) {
                    String line = pageList.get(i).replaceAll("[、：一二三四五六七八九十]*[：:]*", "");
                    if (line.equals("男") || line.equals("女")) {
                        excelMap.put("性别", line);
                        continue;
                    }
                    if (Convert.toInt(line, 100).compareTo(100) < 0) {
                        excelMap.put("年龄", line);
                        continue;
                    }
                    if (fields.contains(line)) {
                        excelMap.put(line, getListItemByIndex(pageList, i + 1));
                        continue;
                    }
                    if (line.equals("职业史")) {
                        //TODO
                        List<String> keys = new ArrayList<>();
                        for (int x = i + 1; x <= i + 5; x++) {
                            String subLine = pageList.get(x);
                            excelMap.put(subLine, new ArrayList<>());
                            keys.add(subLine);
                        }
                        for (int x = i + 6; x < pageList.size(); x++) {
                            int keyIndex = (x - i - 6) % 5;
                            String key = keys.get(keyIndex);
                            String subLine = pageList.get(x);
                            List<String> his = (List<String>) excelMap.getOrDefault(key, new ArrayList<>());
                            if (subLine.contains("受检人签字")) {
                                if (CollectionUtils.isEmpty(his)) {
                                    his.add("/");
                                }
                                excelMap.put(key, his);
                                Integer careerSize = ((List<String>) excelMap.getOrDefault("有害因素", new ArrayList<>())).size();
                                excelMap.put("careerSize", (careerSize > 0 ? careerSize : 1));
                                break;
                            }
                            his.add(subLine);
                            excelMap.put(key, his);
                        }
                        break;
                    }
                }
            } else {
                String name = Convert.toStr(excelMap.getOrDefault("姓名", ""));
                String tjCode = Convert.toStr(excelMap.getOrDefault("体检编号", ""));
                Map<String, String> fields = new HashMap<>();
                fields.put("既往病史", "急慢性职业病史");

                List<String> drinkHis = new ArrayList<>();
                drinkHis.add("吸烟状况");
                drinkHis.add("饮酒状况");
                //子field顺序
                List<String> tabFieldMap = Arrays.asList("-般检查", "般检查", "一般检查",  "血压", "内科检查 (职)",
                        "内科检查（职）", "内科检查(职)", "神经科检查（职）", "神经科检查 (职)", "神经科检查(职)", "外科",
                        "耳鼻喉科检查(职)", "耳鼻喉科检查（职）", "耳鼻喉科检查 (职)", "胸片后前位", "十二导心电分析", "肺功能检查",
                        "纯音听阈测试");

                List<String> unitTabFields = Arrays.asList("肝功九项", "肝功项", "尿常规", "血细胞分析",
                        "肾功能", "血脂四项", "血脂项", "空腹血糖");
                List<String> ignoreField = getIgnoreArray(name,tjCode);
                for (int i = 0; i < pageList.size(); i++) {
                    String line = pageList.get(i).replaceAll("[、：一二三四五六七八九十 　]*[:：]*", "")
                            .replaceAll("[(（]+", "(").replaceAll("[)）]+", ")");
                    if (checkIsIgnore(ignoreField, line)) {
                        continue;
                    }
                    if (fields.containsKey(line)) {
                        String value = getListItemByIndex(pageList, i + 1).replaceAll("[、：一二三四五六七八九十]*[:：]*", "");
                        if (value.contains(fields.get(line))) {
                            excelMap.put(line, "/");
                        } else {
                            excelMap.put(line, value);
                        }
                        continue;
                    }
                    if (checkContains(drinkHis, line)) {
                        StringBuilder sb = new StringBuilder();
                        String endLine;
                        if (line.contains("饮酒状况")) {
                            endLine = "年";
                        } else {
                            line = "吸烟状况";
                            endLine = "饮酒状况";
                            ignoreField.add("烟酒史");
                        }
                        for (int j = i + 1; j < pageList.size(); j++) {
                            String subLine = pageList.get(j).replaceAll("[、：一二三四五六七八九十 　]*[:：]*", "")
                                    .replaceAll("[(（]+", "(").replaceAll("[)）]+", ")");
                            if (checkIsIgnore(ignoreField, subLine)) {
                                continue;
                            }
                            if (subLine.contains(endLine)) {
                                i = j - 1;
                                if (line.equals("饮酒状况")) {
                                    sb.append(subLine);
                                }
                                break;
                            }
                            sb.append(subLine);
                        }
                        excelMap.put(line, sb.toString());
                        continue;
                    }
                    if (checkContains(tabFieldMap, line)) {
                        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                        i = tabFieldProcess(line, i + 1, pageList, ignoreField, map);
                        if(line.contains("般检查")){
                            excelMap.put("一般检查", map);
                        }else{
                            excelMap.put(line, map);
                        }
                        continue;
                    }
                    if (checkContains(unitTabFields, line)) {
                        LinkedHashMap<String, Map<String, String>> map = new LinkedHashMap<>();
                        i = unitTabFieldProcess(i + 1, pageList, ignoreField, map);
                        excelMap.put(line, map);
                        continue;
                    }
                    if (line.contains("DR检查报告单")) {
                        LinkedHashMap<String, String> map = new LinkedHashMap<>();
                        i = reportImgProcess(i + 1, pageList, ignoreField, map);
                        excelMap.put(line, map);
                        continue;
                    }
                    if (line.matches("肺功能[检测]{0,}报告[单]{0,}")) {
                        LinkedHashMap<String, Map<String, String>> map = new LinkedHashMap<>();
                        i = reportImgProcess1(i + 1, pageList, ignoreField, map);
                        excelMap.put("肺功能检测报告", map);
                        continue;
                    }
                    if (line.contains("听力测试报告单")) {
                        LinkedHashMap<String, String> map = new LinkedHashMap<>();
                        i = reportImgProcess2(i + 1, pageList, ignoreField, map);
                        excelMap.put("听力测试报告单", map);
                        continue;
                    }
                    if (line.contains("体检结论")) {
                        LinkedHashMap<String, String> map = new LinkedHashMap<>();
                        i = reportImgProcess3(i, pageList, ignoreField, map);
                        excelMap.put(line, map);
                        continue;
                    }

                }
            }
        }
        return excelMap;
    }

    private static List<String> splitString(String str) {
        List<String> igList = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            Character ch = str.charAt(i);
            for (Character subCh : str.substring(i + 1).toCharArray()) {
                igList.add(new String(new char[]{ch, subCh}));
            }
        }
        return igList;
    }

    private static List<String> getIgnoreArray(String name,String tjCode) {
        List<String> igList = new ArrayList<>(Arrays.asList("体检号", "体检日期", "姓名", "项目名称", "结果", "参考值", "单位",
                "检查结果", "上次检查结果", "预测值", "之前", "预值", "%预值", "体检号", "体检日期", tjCode));
        if (StringUtils.isNotBlank(name)) {
            igList.addAll(splitString(name));
        }
        return igList;
    }

    private static int reportImgProcess2(int i, List<String> pageList, List<String> ignoreLine, LinkedHashMap<String, String> map) {
        for (int j = i; j < pageList.size(); j++) {
            String line = pageList.get(j).replaceAll("[、：一二三四五六七八九十]*[:]*", "");
            if (line.contains("平均听阈值及结论语")) {
                for (int x = j + 1; x < pageList.size(); x++) {
                    String subLine = pageList.get(x).replaceAll("[、：一二三四五六七八九十]*[:：]*", "");
                    if (subLine.contains("平均听阈") || subLine.contains("矫正值")) {
                        map.put(subLine, getListItemByIndex(pageList, x + 1));
                    }
                    if (subLine.equals("语频矫正值")) {
                        map.put("结论", getListItemByIndex(pageList, x + 2));
                        i = x;
                        break;
                    }
                }
                break;
            }
        }
        return i;
    }

    private static int reportImgProcess1(int i, List<String> pageList, List<String> ignoreFields, LinkedHashMap<String, Map<String, String>> itemMap) {
        String lastLine = null;
        List<String> checkRes = new ArrayList<>();
        for (int x = i; x < pageList.size(); x++) {
            String line = pageList.get(x).replaceAll("[、： 　]*[:：]*", "")
                    .replaceAll("[(（]+", "(").replaceAll("[)）]+", ")");
            if (line.contains("单位")) {
                for (int j = x ; j < pageList.size(); j++) {
                    String subLine = pageList.get(j);
                    if (checkIsIgnore(ignoreFields, subLine)) {
                        continue;
                    }
                    if (subLine.matches("[0-9]*%")) {
                        if (pageList.get(j - 1).matches("[+-]?[0-9]+(\\.[0-9]+)?")) {
                            checkRes.add(pageList.get(j - 1));
                        }
                        checkRes.add(subLine);
                        continue;
                    }
                    if (subLine.matches("[(（]+[0-9]{1,3}[)）]+|%|I/s|[+-]?[0-9]+(\\.[0-9]+)?")) {
                        continue;
                    }
                    if (hasChinese(subLine)) {
                        Map<String, String> lineMap = new HashMap<>();
                        lineMap.put("之前", getListItemByIndex(checkRes, 0));
                        lineMap.put("%预值", getListItemByIndex(checkRes, 1));
                        itemMap.put(lastLine, lineMap);
                        break;
                    }
                    if (StringUtils.isNotBlank(lastLine) && !subLine.equals(lastLine)) {
                        Map<String, String> lineMap = new HashMap<>();
                        lineMap.put("之前", getListItemByIndex(checkRes, 0));
                        lineMap.put("%预值", getListItemByIndex(checkRes, 1));
                        itemMap.put(lastLine, lineMap);
                        checkRes.clear();
                    }
                    lastLine = subLine;
                    j++;
                }
                i = x;
                break;
            }

        }
        return i;
    }


    private static boolean hasChinese(String str) {
        if (StringUtils.isNotBlank(str)) {
            for (String s : str.split("")) {
                if (s.matches("[\\u4e00-\\u9fa5]+")) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int reportImgProcess3(int i, List<String> pageList, List<String> ignoreLine, LinkedHashMap<String, String> map) {
        for (int j = i; j < pageList.size(); j++) {
            String line = pageList.get(j).replaceAll("[、： 　]*[:：]*", "")
                    .replaceAll("[(（]+", "(").replaceAll("[)）]+", ")");
            if (checkIsIgnore(ignoreLine, line)) {
                continue;
            }
            if (line.contains("体检结论")) {
                StringBuilder sb = new StringBuilder();
                for (int x = j + 1; x < pageList.size(); x++) {
                    String subLine = pageList.get(x);
                    if (subLine.contains("建议")) {
                        map.put(line, sb.toString());
                        j = x - 1;
                        break;
                    } else {
                        sb.append(pageList.get(x));
                    }
                }
            }
            if (line.contains("建议")) {
                StringBuilder sb = new StringBuilder();
                for (int x = j; x < pageList.size(); x++) {
                    String subLine = pageList.get(x);
                    if (subLine.matches("[\\s\\S]{0,}医师[:：]+[\\s\\S]{0,}")) {
                        map.put(line, sb.toString());
                        j = x;
                        break;
                    } else {
                        sb.append(pageList.get(x));
                    }
                }
                i = j;
                break;
            }
        }
        return i;
    }

    private static int reportImgProcess(int i, List<String> pageList, List<String> ignoreLine, LinkedHashMap<String, String> map) {
        for (int j = i; j < pageList.size(); j++) {
            String line = pageList.get(j).replaceAll("[、： 　]*[:：]*", "")
                    .replaceAll("[(（]+", "(").replaceAll("[)）]+", ")");
            if (checkIsIgnore(ignoreLine, line)) {
                continue;
            }
            if (line.contains("检查所见")) {
                StringBuilder sb = new StringBuilder();
                for (int x = j; x < pageList.size(); x++) {
                    String subLine = pageList.get(x);
                    if (subLine.contains("诊断意见")) {
                        map.put(line, sb.toString());
                        j = x;
                        break;
                    } else {
                        sb.append(pageList.get(x));
                    }
                }
            }
            if (line.contains("检查所见")) {
                StringBuilder sb = new StringBuilder();
                for (int x = j; x < pageList.size(); x++) {
                    String subLine = pageList.get(x);
                    if (subLine.contains("医生")) {
                        map.put(line, sb.toString());
                        j = x;
                        break;
                    } else {
                        sb.append(pageList.get(x));
                    }
                }
                i = j;
                break;
            }
        }
        return i;
    }

    private static int unitTabFieldProcess(Integer i, List<String> pageList, List<String> ignoreFields, LinkedHashMap<String, Map<String, String>> itemMap) {
        String lastLine = null;
        List<String> checkRes = new ArrayList<>();
        for (int x = i; x < pageList.size(); x++) {
            String line = pageList.get(x).replaceAll("[、： 　]*[:：]*", "")
                    .replaceAll("[(（]+", "(").replaceAll("[)）]+", ")");
            if (checkIsIgnore(ignoreFields, line)) {
                continue;
            }
            if (line.matches("[+-≤]?[0-9]+(\\.[0-9]+)?[个↑+]?")) {
                checkRes.add(line.replaceAll("[个↑]", ""));
                continue;
            }
            if (line.matches("^[×a-zA-Z0-9\\^.-]{0,}[/% -]{0,}[0-9a-zA-Z.-]{0,}$|一")) {
                continue;
            }
            if (line.contains("本次检查者")) {
                Map<String, String> lineMap = new HashMap<>();
                lineMap.put("检查结果", getListItemByIndex(checkRes, 0));
                lineMap.put("上次检查结果", getListItemByIndex(checkRes, 1));
                itemMap.put(lastLine, lineMap);
                i = x;
                break;
            }
            if (StringUtils.isNotBlank(lastLine) && !line.equals(lastLine)) {
                Map<String, String> lineMap = new HashMap<>();
                lineMap.put("检查结果", getListItemByIndex(checkRes, 0));
                lineMap.put("上次检查结果", getListItemByIndex(checkRes, 1));
                itemMap.put(lastLine, lineMap);
                checkRes.clear();
            }
            lastLine = line;
            i = x;
        }
        return i;
    }

    private static int tabFieldProcess(String tabField, Integer i, List<String> pageList, List<String> ignoreFields, LinkedHashMap<String, Object> excelMap) {
        if (tabField.equals("纯音听阈测试")) {
            StringBuilder sb = new StringBuilder();
            for (int x = i; x < pageList.size(); x++) {
                String xLine = pageList.get(x).replaceAll("[ 　]{0,}", "")
                        .replaceAll("[(（]+", "(").replaceAll("[)）]+", ")");
                if (checkIsIgnore(ignoreFields, xLine)) {
                    continue;
                }
                if (xLine.equals("结论")) {
                    excelMap.put("描述", sb.toString());
                    excelMap.put(xLine, getListItemByIndex(pageList, x + 1));
                    i = x + 1;
                    break;
                } else if (!xLine.equals("描述")) {
                    sb.append(xLine);
                }
            }
        } else if (tabField.equals("肺功能检查")) {
            String keyNoVal = null;
            for (int x = i; x < pageList.size(); x++) {
                String xLine = pageList.get(x).replaceAll("[ 　]{0,}", "")
                        .replaceAll("[(（]+", "(").replaceAll("[)）]+", ")");
                if (checkIsIgnore(ignoreFields, xLine)) {
                    continue;
                }
                if (xLine.equals("结论")) {
                    excelMap.put(xLine, getListItemByIndex(pageList, x + 1));
                    i = x + 1;
                    break;
                } else if (!xLine.equals("描述")) {
                    if (StringUtils.isNumeric(xLine)) {
                        excelMap.put(keyNoVal, xLine);
                    } else {
                        List<String> fields = Arrays.asList(xLine.split("[:：]"));
                        if (fields.size() == 1) {
                            keyNoVal = fields.get(0);
                        }else{
                            excelMap.put(getListItemByIndex(fields, 0), getListItemByIndex(fields, 1));
                        }
                    }
                }
            }
        } else if (tabField.equals("胸片后前位") || tabField.equals("十二导心电分析")) {
            for (int x = i; x < pageList.size(); x++) {
                String xLine = pageList.get(x);
                if (checkIsIgnore(ignoreFields, xLine)) {
                    continue;
                }
                if (xLine.equals("结论")) {
                    excelMap.put(tabField, getListItemByIndex(pageList, x + 1));
                    i = x + 1;
                    break;
                }
            }
        } else {
            for (int x = i; x < pageList.size(); x++) {
                i = x;
                String line = pageList.get(x).replaceAll("[、： 　]*[:：]*", "")
                        .replaceAll("[(（]+", "(").replaceAll("[)）]+", ")");
                if (checkIsIgnore(ignoreFields, line)) {
                    continue;
                }
                if (line.contains("检查者")) {
                    break;
                }
                if (line.contains("般状况")) {
                    excelMap.put("一般状况", getListItemByIndex(pageList, x + 1));
                } else {
                    excelMap.put(line, getListItemByIndex(pageList, x + 1));
                }
                if (line.contains("其他")) {
                    break;
                }
                x++;
            }
        }
        return i;
    }

    private static String getListItemByIndex(List<String> pageList, int index) {
        if (index <= pageList.size() - 1) {
            return pageList.get(index);
        }
        return "";
    }


    private static boolean checkIsIgnore(List<String> ignoreField, String line) {
        return checkContains(ignoreField, line) ? true : line.matches("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}");
    }

    private static boolean checkContains(List<String> containList, String line) {
        for (String ignoreLine : containList) {
            if (line.contains(ignoreLine)) {
                return true;
            }
        }
        return false;
    }

    private static void printMessage(final InputStream input) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Reader reader = new InputStreamReader(input);
                BufferedReader bf = new BufferedReader(reader);
                String line = null;
                try {
                    while ((line = bf.readLine()) != null) {
                        System.out.println(line);
                    }
                    reader.close();
                    bf.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static List<List<String>> readPdfByOcr(String pdfPath) throws IOException, InterruptedException {
        List<List<String>> content = new ArrayList<>();
        String pythonPath = ResourceUtils
                .getURL("classpath:python/").getPath().replaceFirst("/", "");
        String[] args = new String[]{"python", pythonPath + "pdf_ocr.py", pdfPath};
        ProcessBuilder builder = new ProcessBuilder(args);
        Process process = builder.start();
        Reader reader = new InputStreamReader(process.getInputStream(),"gb2312");
        BufferedReader bf = new BufferedReader(reader);
        String line = null;
        List<String> page = null;
        String prefix = "[" + DateFormatUtils.format(new Date(), "yyyy/MM/dd");
        List<String> pageIndexList = Arrays.asList("1", "2", "3", "4");
        while ((line = bf.readLine()) != null) {
            if (!line.startsWith(prefix)) {
                if (line.startsWith("page")) {
                    String pageIndex = line.replace("page ", "");
                    if (page != null && page.size() > 3) {
                        for (int i = 3; i > 0; i--) {
                            String ignoreFoot = page.get(page.size() - i);
                            if (ignoreFoot.matches("[" + (Convert.toInt(pageIndex) - 1) + "]{0,}[/／ ]{0,}[0-9]{0,}")) {
                                page.remove(page.size() - i);
                            }
                        }
                    }
                    if (pageIndexList.contains(pageIndex)) {
                        if (!CollectionUtils.isEmpty(page)) {
                            content.add(page);
                        }
                        page = new ArrayList<>();
                    }
                } else {
                    page.add(line.replaceAll("�[0-9]{0,}�[0-9]{0,}", "").replace("�", ""));
                }
            }
        }
        if (!CollectionUtils.isEmpty(page)) {
            content.add(page);
        }
        reader.close();
        bf.close();
        process.waitFor();
        printMessage(process.getErrorStream());
        return content;
    }

}
