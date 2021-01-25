package yui.hesstina.mistakes.concurrenttool.threadlocal;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * ThreadLocal 滥用
 *
 * @author YuI
 * @date 2021/1/25 12:48
 * @since 1.0
 */
@RestController
@RequestMapping("threadlocal")
@Slf4j
public class ThreadLocalMisuseController {

    private static final ThreadLocal<Integer> currentUser = ThreadLocal.withInitial(() -> null);

    /**
     * 错误用法 <br/>
     * tomcat 最大线程调整为 1
     *
     * @param userId 用户 id
     * @return {@linkplain Map}
     */
    @GetMapping("wrong")
    public Map<String, String> wrong(@RequestParam("userId") Integer userId) {
        // 设置用户信息之前先查询一次ThreadLocal中的用户信息
        String before = Thread.currentThread().getName() + " - " + currentUser.get();
        // 设置用户信息到 ThreadLocal
        currentUser.set(userId);
        // 设置用户信息之后再查询一次 ThreadLocal 中的用户信息
        String after = Thread.currentThread().getName() + " - " + currentUser.get();

        // 汇总输出两次查询结果
        Map<String, String> result = new HashMap<>(2);
        result.put("before", before);
        result.put("after", after);
        return result;
    }

    /**
     * 正确用法 <br/>
     * tomcat 最大线程调整为 1 <br/>
     * 释放 ThreadLocal
     *
     * @param userId 用户 id
     * @return {@linkplain Map}
     */
    @GetMapping("right")
    public Map<String, String> right(@RequestParam("userId") Integer userId) {
        // 设置用户信息之前先查询一次ThreadLocal中的用户信息
        String before = Thread.currentThread().getName() + " - " + currentUser.get();
        // 设置用户信息到 ThreadLocal
        currentUser.set(userId);

        try {
            // 设置用户信息之后再查询一次 ThreadLocal 中的用户信息
            String after = Thread.currentThread().getName() + " - " + currentUser.get();
            // 汇总输出两次查询结果
            Map<String, String> result = new HashMap<>(2);

            result.put("before", before);
            result.put("after", after);
            return result;
        } finally {
            currentUser.remove();
        }
    }
}
