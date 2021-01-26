package yui.hesstina.mistakes.concurrenttool.copyonwritelistmisuse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * CopyOnWriteListMisuse 滥用 <br/>
 * Controller
 *
 * @author YuI
 * @date 2021/1/26 20:57 
 * @since 1.0
 **/
@RestController
@RequestMapping("copyonwritelistmisuse")
@Slf4j
public class CopyOnWriteListMisuseController {

    /**
     * 测试并发写
     *
     * @return {@linkplain Map}
     */
    @GetMapping("write")
    public Map<String, Integer> write() {
        List<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<>());
        int loopCount = 100000;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("Write:copyOnWriteArrayList");
        IntStream.range(1, loopCount).parallel().forEach(i -> copyOnWriteArrayList.add(ThreadLocalRandom.current().nextInt(loopCount)));
        stopWatch.stop();
        stopWatch.start("Write:synchronizedList");
        IntStream.range(1, loopCount).parallel().forEach(i -> synchronizedList.add(ThreadLocalRandom.current().nextInt(loopCount)));
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());

        Map<String, Integer> result = new HashMap<>(2);
        result.put("copyOnWriteArrayList", copyOnWriteArrayList.size());
        result.put("synchronizedList", synchronizedList.size());
        return result;
    }

    /**
     * 测试并发读
     *
     * @return {@linkplain Map}
     */
    @GetMapping("read")
    public Map<String, Integer> read() {
        List<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<>());
        addAll(copyOnWriteArrayList);
        addAll(synchronizedList);
        int loopCount = 100000;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("Read:copyOnWriteArrayList");
        IntStream.range(1, loopCount).parallel().forEach(i -> copyOnWriteArrayList.get(ThreadLocalRandom.current().nextInt(i)));
        stopWatch.stop();
        stopWatch.start("Read:synchronizedList");
        IntStream.range(1, loopCount).parallel().forEach(i -> synchronizedList.get(ThreadLocalRandom.current().nextInt(i)));
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());

        Map<String, Integer> result = new HashMap<>(2);
        result.put("copyOnWriteArrayList", copyOnWriteArrayList.size());
        result.put("synchronizedList", synchronizedList.size());
        return result;
    }

    /**
     * 给传入的数组增加内容
     *
     * @param list 数组
     */
    private void addAll(List<Integer> list) {
        list.addAll(IntStream.range(1, 100000).boxed().collect(Collectors.toList()));
    }

}
