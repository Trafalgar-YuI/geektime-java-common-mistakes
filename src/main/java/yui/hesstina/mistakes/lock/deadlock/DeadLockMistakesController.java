package yui.hesstina.mistakes.lock.deadlock;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 死锁问题 <br/>
 * Controller
 *
 * @author YuI
 * @date 2021/1/29 16:02
 * @since 1.0
 */
@RestController
@RequestMapping("deadlockmistakes")
@Slf4j
public class DeadLockMistakesController {

    private ConcurrentMap<String, Item> items;

    public DeadLockMistakesController() {
        items = new ConcurrentHashMap<>();
        IntStream.range(0, 10).forEach(i -> items.put("item" + i, new Item("item" + i)));
    }

    @GetMapping("wrong")
    public Map<String, Long> wrong() {
        long begin = System.currentTimeMillis();

        // 并发进行 100 次下单操作，统计成功次数
        long success = IntStream.rangeClosed(1, 100).parallel()
                .mapToObj(i -> {
                    List<Item> cart = createCart();
                    return createOrder(cart);
                })
                .filter(result -> result)
                .count();

        log.info("success:{} totalRemaining:{} took:{}ms items:{}",
                success,
                items.values().stream().map(item -> item.remaining).reduce(0, Integer::sum),
                System.currentTimeMillis() - begin, items);

        return ImmutableMap.of("result", success);
    }

    @GetMapping("right")
    public Map<String, Long> right() {
        long begin = System.currentTimeMillis();

        // 并发进行 100 次下单操作，统计成功次数，商品用排序的方式，能够避免死锁
        long success = IntStream.rangeClosed(1, 100).parallel()
                .mapToObj(i -> {
                    List<Item> cart = createCart().stream()
                            .sorted(Comparator.comparing(Item::getName))
                            .collect(Collectors.toList());
                    return createOrder(cart);
                })
                .filter(result -> result)
                .count();

        log.info("success:{} totalRemaining:{} took:{}ms items:{}",
                success,
                items.values().stream().map(item -> item.remaining).reduce(0, Integer::sum),
                System.currentTimeMillis() - begin, items);

        return ImmutableMap.of("result", success);
    }

    /**
     * 创建订单
     *
     * @param order {@linkplain Item}
     * @return 是否创建成功
     */
    private boolean createOrder(List<Item> order) {
        List<ReentrantLock> locks = Lists.newArrayList();

        for (Item item : order) {
            try {
                if (item.lock.tryLock(10, TimeUnit.SECONDS)) {
                    locks.add(item.lock);
                } else {
                    locks.forEach(ReentrantLock::unlock);
                    return false;
                }
            } catch (InterruptedException e) {
            }
        }

        try {
            order.forEach(item -> item.remaining--);
        } finally {
            locks.forEach(ReentrantLock::unlock);
        }

        return true;
    }

    /**
     * 创建购物车
     *
     * @return {@linkplain List}
     */
    private List<Item> createCart() {
        return IntStream.rangeClosed(1, 3)
                .mapToObj(i -> "item" + ThreadLocalRandom.current().nextInt(items.size()))
                .map(name -> items.get(name))
                .collect(Collectors.toList());
    }

    @Data
    @RequiredArgsConstructor
    static class Item {
        /**
         * 商品名
         */
        final String name;
        /**
         * 库存剩余
         */
        int remaining = 1000;
        /**
         * ToString 不包含这个字段
         */
        @ToString.Exclude
        ReentrantLock lock = new ReentrantLock();
    }

}
