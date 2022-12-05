package cn.iocoder.yudao.framework.quartz.config;

import com.alibaba.ttl.TtlRunnable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务 Configuration
 */
@Configuration
@EnableAsync
public class YudaoAsyncAutoConfiguration {

    @Bean
    public BeanPostProcessor threadPoolTaskExecutorBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (!(bean instanceof ThreadPoolTaskExecutor)) {
                    return bean;
                }
                // 修改提交的任务，接入 TransmittableThreadLocal
                ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) bean;
                executor.setTaskDecorator(TtlRunnable::get);
                return executor;
            }

        };
    }

    @Bean("threadPoolTaskExecutor")
    public Executor getThreadPool() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置最大线程数
        executor.setMaxPoolSize(10);
        // 配置核心线程数
        executor.setCorePoolSize(5);
        // 配置队列大小
        executor.setQueueCapacity(1024);
        // 线程存活时间
        executor.setKeepAliveSeconds(300);
        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("read_pdf_");
        // 线程池对拒绝任务(无线程可用)的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }

}
