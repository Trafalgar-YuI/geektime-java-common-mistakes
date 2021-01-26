package yui.hesstina.mistakes.concurrenttool.copyonwritelistmisuse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CopyOnWriteListMisuse 滥用 <br/>
 * 启动类
 *
 * @author YuI
 * @date 2021/1/26 20:56 
 * @since 1.0
 **/
@SpringBootApplication
public class CopyOnWriteListMisuseApplication {

    public static void main(String[] args) {
        SpringApplication.run(CopyOnWriteListMisuseApplication.class, args);
    }

}
