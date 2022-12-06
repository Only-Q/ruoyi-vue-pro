package cn.iocoder.yudao.module.system.util;

import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Future;

public class AsyncUtils {
    /**
     * 判断调度任务是否完成.
     * @param futureList 线程执行情况.
     * @return Pair<Integer,Boolean> .
     */
    public static <T> Pair<BigDecimal,Boolean> isStop(final List<Future<T>> futureList) {
        BigDecimal success = BigDecimal.ZERO;
        Boolean flag = true;
        for (Future<T> future : futureList) {
            //获取future成功完成状态，如果想要限制每个任务的超时时间，取消本行的状态判断+future.get(1000*1, TimeUnit.MILLISECONDS)+catch超时异常使用即可。
            if (!future.isCancelled() && !future.isDone()) {
                flag = false;
            } else {
                success = success.add(BigDecimal.ONE);
            }
        }
        return Pair.of(success, flag);
    }
}
