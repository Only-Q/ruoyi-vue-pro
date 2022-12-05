package cn.iocoder.yudao.module.system.util;

import org.apache.poi.ss.formula.functions.T;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class AsyncUtils {
    /**
     * 判断调度任务是否完成.
     * @param futureList 线程执行情况.
     * @return 是否完成.
     * @param Map<String,Object> .
     */
    public static Map<String,Integer> isStop(final List<Future<Object>> futureList) {
        Map<String,Integer> map = new HashMap<>();
        int success = 0;
        int flag = 1;
        for (Future<Object> future : futureList) {
            //获取future成功完成状态，如果想要限制每个任务的超时时间，取消本行的状态判断+future.get(1000*1, TimeUnit.MILLISECONDS)+catch超时异常使用即可。
            if (!future.isCancelled() && !future.isDone()) {
                flag = 0;
            }else {
                success++;
            }
        }
        map.put("flag",flag);
        map.put("sucess",success);
        return map;
    }
}
