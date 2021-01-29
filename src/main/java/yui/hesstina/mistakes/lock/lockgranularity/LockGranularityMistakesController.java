package yui.hesstina.mistakes.lock.lockgranularity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 * @author YuI
 * @date 2021/1/28 14:07
 * @since 1.0
 */
@RestController
@RequestMapping("lockgranularitymistakes")
@Slf4j
public class LockGranularityMistakesController {

    private List<Integer> data = new ArrayList<>();

    /**
     * 不涉及共享资源的慢方法
     */
    private void slow() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
        }
    }

    /**
     * 错误的加锁方法
     *
     * @return {@linkplain Map}
     */
    @GetMapping("/wrong")
    public Map<String, Integer> wrong() {
        long begin = System.currentTimeMillis();
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
            // 加锁粒度太粗了
            synchronized (this) {
                slow();
                data.add(i);
            }
        });

        log.info("took:{}", System.currentTimeMillis() - begin);

        return ImmutableMap.of("result", data.size());
    }

    /**
     * 正确的加锁方法
     *
     * @return {@linkplain Map}
     */
    @GetMapping("/right")
    public Map<String, Integer> right() {
        long begin = System.currentTimeMillis();
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
            slow();
            // 只对共享的 list 加锁
            synchronized (this) {
                data.add(i);
            }
        });

        log.info("took:{}", System.currentTimeMillis() - begin);

        return ImmutableMap.of("result", data.size());
    }
}
