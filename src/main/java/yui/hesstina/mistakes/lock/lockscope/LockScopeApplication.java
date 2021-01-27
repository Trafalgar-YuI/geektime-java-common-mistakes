package yui.hesstina.mistakes.lock.lockscope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 锁的范围相关问题 <br/>
 * 启动类
 *
 * @author YuI
 * @date 2021/1/27 10:59
 * @since 1.0
 */
@SpringBootApplication
public class LockScopeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LockScopeApplication.class, args);
    }

}
