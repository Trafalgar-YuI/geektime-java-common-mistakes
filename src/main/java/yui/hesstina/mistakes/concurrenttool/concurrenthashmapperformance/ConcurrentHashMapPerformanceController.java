package yui.hesstina.mistakes.concurrenttool.concurrenthashmapperformance;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * ConcurrentHashMap 用法演示 <br/>
 * Controller
 *
 * @author YuI
 * @date 2021/1/26 2:08 
 * @since 1.0
 **/
@RestController
@RequestMapping("concurrenthashmapperformance")
@Slf4j
public class ConcurrentHashMapPerformanceController {

    // 循环次数
    private static int LOOP_COUNT = 10000000;
    // 线程数量
    private static int THREAD_COUNT = 10;
    // 元素数量
    private static int ITEM_COUNT = 10;

    /**
     * 比较两种用法的效率
     *
     * @return OK
     */
    @GetMapping("compare")
    public String compare() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("normal use");
        Map<String, Long> normalUse = normalUse();
        Assert.isTrue(normalUse.size() == ITEM_COUNT, "normal use is error");
        Assert.isTrue(normalUse.values().stream().mapToLong(l -> l).reduce(0, Long::sum) == LOOP_COUNT,
                "normal use count error");
        stopWatch.stop();
        stopWatch.start("good use");
        Map<String, Long> goodUse = goodUse();
        Assert.isTrue(goodUse.size() == ITEM_COUNT, "good use is error");
        Assert.isTrue(goodUse.values().stream().mapToLong(l -> l).reduce(0, Long::sum) == LOOP_COUNT,
                "good use count error");
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());
        return "OK";
    }

    /**
     * 一般用法
     *
     * @return {@linkplain Map}
     */
    @SneakyThrows
    private Map<String, Long> normalUse() {
        ConcurrentHashMap<String, Long> freqs = new ConcurrentHashMap<>(ITEM_COUNT);
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, LOOP_COUNT).parallel().forEach(i -> {
                    // 获得一个随机的 Key
                    String key = "item" + ThreadLocalRandom.current().nextInt(ITEM_COUNT);
                    synchronized (freqs) {
                        if (freqs.containsKey(key)) {
                            // Key 存在则 +1
                            freqs.put(key, freqs.get(key) + 1);
                        } else {
                            // Key 不存在则初始化为 1
                            freqs.put(key, 1L);
                        }
                    }
                }
        ));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        return freqs;
    }

    /**
     * 推荐用法
     *
     * @return {@linkplain Map}
     */
    @SneakyThrows
    private Map<String, Long> goodUse() {
        ConcurrentHashMap<String, LongAdder> freqs = new ConcurrentHashMap<>(ITEM_COUNT);
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, LOOP_COUNT).parallel().forEach(i -> {
                    // 获得一个随机的 Key
                    String key = "item" + ThreadLocalRandom.current().nextInt(ITEM_COUNT);
                    freqs.computeIfAbsent(key, s -> new LongAdder()).increment();
                }
        ));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        return freqs.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().longValue()
                ));
    }

}
