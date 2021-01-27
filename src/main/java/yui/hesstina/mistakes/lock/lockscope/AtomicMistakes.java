package yui.hesstina.mistakes.lock.lockscope;

import java.util.stream.IntStream;

import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * 原子性相关问题
 *
 * @author YuI
 * @date 2021/1/27 11:01
 * @since 1.0
 */
@Slf4j
public class AtomicMistakes {

    private volatile int a = 1;
    private volatile int b = 1;

    public synchronized void add() {
        log.info("add start");

        IntStream.range(0, 1000000)
                .forEach(i -> {
                    a++;
                    b++;
                });

        log.info("add done");
    }

    public void compare() {
        log.info("compare start");

        IntStream.range(0, 1000000)
                .forEach(i -> {
                    if (a < b) {
                        log.info("a: {}, b: {}, a < b: {}", a, b, a < b);
                    }
                });

        log.info("compare done");
    }

    public synchronized void compareRight() {
        log.info("compare start");

        IntStream.range(0, 1000000)
                .forEach(i -> {
                    Assert.isTrue(a == b, "compareRight: a != b");
                    if (a < b) {
                        log.info("a: {}, b: {}, a < b: {}", a, b, a < b);
                    }
                });

        log.info("compare done");
    }

}
