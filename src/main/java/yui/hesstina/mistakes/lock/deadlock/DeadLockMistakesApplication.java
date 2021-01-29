package yui.hesstina.mistakes.lock.deadlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 死锁问题 <br/>
 * 启动类
 *
 * @author YuI
 * @date 2021/1/29 16:01
 * @since 1.0
 */
@SpringBootApplication
public class DeadLockMistakesApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeadLockMistakesApplication.class, args);
    }

}
