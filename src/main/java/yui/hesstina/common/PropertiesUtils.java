package yui.hesstina.common;

import java.util.Properties;

import org.yaml.snakeyaml.Yaml;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * tomcat 资源配置工具类
 *
 * @author YuI
 * @date 2021/1/25 12:41
 * @since v1.0
 */
@Slf4j
@UtilityClass
public class PropertiesUtils {

    @SneakyThrows
    public static void loadPropertiesSource(Class<?> clazz, String fileName) {
        Properties properties = new Properties();
        properties.load(clazz.getResourceAsStream(fileName));
        properties.forEach((k, v) -> {
            log.info("{} = {}", k, v);
            System.setProperty(k.toString(), v.toString());
        });
    }

}
