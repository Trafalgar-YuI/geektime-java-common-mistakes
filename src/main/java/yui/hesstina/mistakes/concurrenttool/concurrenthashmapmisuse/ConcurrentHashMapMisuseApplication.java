package yui.hesstina.mistakes.concurrenttool.concurrenthashmapmisuse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ConcurrentHashMap 滥用 <br/>
 * 启动类
 *
 * @author YuI
 * @date 2021/1/26 1:27 
 * @since 1.0
 **/
@SpringBootApplication
public class ConcurrentHashMapMisuseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConcurrentHashMapMisuseApplication.class, args);
    }

}
