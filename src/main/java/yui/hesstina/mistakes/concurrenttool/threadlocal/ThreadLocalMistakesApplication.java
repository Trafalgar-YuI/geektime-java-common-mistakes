package yui.hesstina.mistakes.concurrenttool.threadlocal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.SneakyThrows;
import yui.hesstina.common.PropertiesUtils;

/**
 * 并发工具 <br/>
 * ThreadLocal 相关
 *
 * @author YuI
 * @date 2021/1/25 12:38
 * @since 1.0
 */
@SpringBootApplication
public class ThreadLocalMistakesApplication {

    @SneakyThrows
    public static void main(String[] args) {
        PropertiesUtils.loadPropertiesSource(ThreadLocalMistakesApplication.class,
                "/META-INF/concurrenttool/threadlocal/tomcat.properties");

        SpringApplication.run(ThreadLocalMistakesApplication.class, args);
    }

}
