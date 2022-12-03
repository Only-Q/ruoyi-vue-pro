package cn.iocoder.yudao.module.system.service.pdfread;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.iocoder.yudao.module.system.util.AsyncUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class PdfReadServiceImpl implements PdfReadService {

    @Autowired
    private PdfReadExecutor pdfReadExecutor;

    @Override
    public List<List<String>> readPdfs(List<String> pdfPaths) throws ExecutionException, InterruptedException {
        StopWatch stopWatch = new StopWatch("解析pdf");
        stopWatch.start("开始解析");
        List<Future<LinkedHashMap<String, Object>>> pdfFuture = new ArrayList<>();
        for (String pdfPath : pdfPaths) {
            Future<LinkedHashMap<String, Object>> futureRes = pdfReadExecutor.readPdf(pdfPath);
            pdfFuture.add(futureRes);
        }

        while (!AsyncUtils.isStop(pdfFuture)) {
            Thread.sleep(60 * 1000);
        }
        stopWatch.stop();
        stopWatch.start("整合数据");
        Integer careerSize = 0;
        LinkedHashMap<String, Set<String>> fieldMap = new LinkedHashMap<>();
        Map<String, Set<String>> subMapSize = new HashMap<>();
        List<LinkedHashMap<String, Object>> pdfContents = new ArrayList<>();
        for (Future<LinkedHashMap<String, Object>> future : pdfFuture) {
            LinkedHashMap<String, Object> pdfContent = future.get();
            pdfContents.add(pdfContent);
            Integer subCareerSize = Convert.toInt(pdfContent.get("careerSize"));
            if (NumberUtil.compare(careerSize, subCareerSize) < 0) {
                careerSize = subCareerSize;
            }
            pdfContent.forEach((k, v) -> {
                if (!k.equals("careerSize")) {
                    if (!fieldMap.containsKey(k)) {
                        Set<String> set = new HashSet<>();
                        if (v instanceof Map) {
                            Map<String, Object> vMap = (Map) v;
                            subMapSize.put(k, new HashSet<>(vMap.keySet()));
                            vMap.forEach((k1, v1) -> {
                                if (v1 instanceof Map) {
                                    subMapSize.put(k1, new HashSet<>(((Map) v1).keySet()));
                                }
                            });
                            set = new HashSet<>(vMap.keySet());
                        }
                        fieldMap.put(k, set);
                    } else {
                        if (fieldMap.containsKey(k) && v instanceof Map) {
                            Map<String, Object> vMap = (Map) v;
                            Set<String> fieldSet = fieldMap.getOrDefault(k, new HashSet<>());
                            List<String> subList = (List<String>) CollectionUtils.subtract(vMap.keySet(), fieldSet);
                            Set<String> subFieldSet = subMapSize.getOrDefault(k, new HashSet<>());
                            subFieldSet.addAll(subList);
                            subMapSize.put(k, subFieldSet);
                            if (CollectionUtil.isNotEmpty(subList)) {
                                for (String subF : subList) {
                                    fieldSet.add(subF);
                                    Object v1 = vMap.get(subF);
                                    if (v1 instanceof Map) {
                                        subMapSize.put(subF, new HashSet<>(((Map) v1).keySet()));
                                    }
                                }
                                fieldMap.put(k, fieldSet);
                            }
                        }
                    }
                }
            });
        }

        List<String> firField = new ArrayList<>();
        fieldMap.forEach((k, v) -> {
            firField.add(k);
            if (CollectionUtils.isNotEmpty(v)) {
                firField.addAll(v);
            }
        });
        List<List<String>> excelCon = new ArrayList<>();
        List<String> headList = new ArrayList<>();
        for (int i = 0; i < pdfContents.size(); i++) {
            LinkedHashMap<String, Object> content = pdfContents.get(i);
            List<String> valueList = new ArrayList<>();
            for (int j = 0; j < firField.size(); j++) {
                String field = firField.get(j);
                boolean needHead = i == 0;
                if (needHead) {
                    headList.add(field);
                }
                Object o = content.get(field);
                if (o != null) {
                    if (o instanceof String) {
                        valueList.add(Convert.toStr(o, ""));
                    } else if (o instanceof Map) {
                        valueList.add("");
                        j = getSubMapValByField(needHead, j + 1, valueList, headList, (Map<String, Object>) o, subMapSize, field, true);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (String val : (List<String>) o) {
                            sb.append(val);
                        }
                        valueList.add(sb.toString());
                    }
                    continue;
                }
                valueList.add("");
            }
            excelCon.add(valueList);
        }
        excelCon.add(0, headList);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        return excelCon;
    }

    private Integer getSubMapValByField(boolean needHead, int i, List<String> valueList, List<String> headList,
                                               Map<String, Object> content, Map<String, Set<String>> subMapSize,
                                               String field, boolean indexPlusFlag) {
        for (String subField : subMapSize.get(field)) {
            if (indexPlusFlag) {
                i++;
            }
            if (needHead) {
                headList.add(subField);
            }
            Object o = content.get(subField);
            if (o != null) {
                if (o instanceof Map) {
                    valueList.add("");
                    i = getSubMapValByField(needHead, i, valueList, headList, (Map<String, Object>) o, subMapSize, subField, false);
                } else {
                    valueList.add(Convert.toStr(o, ""));
                }
                continue;
            }
            valueList.add("");
        }
        if (indexPlusFlag) {
            i = i - 1;
        }
        return i;
    }
}
