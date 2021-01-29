package yui.hesstina.mistakes.lock.lockscope;

import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

import lombok.extern.slf4j.Slf4j;

/**
 * 锁的作用域相关问题 <br/>
 * Controller
 *
 * @author YuI
 * @date 2021/1/27 11:11
 * @since 1.0
 */
@RestController
@RequestMapping("lockscope")
@Slf4j
public class LockScopeController {

	@GetMapping("wrong")
	public String wrong() {
		AtomicMistakes mistakes = new AtomicMistakes();
		new Thread(mistakes::add).start();
		new Thread(mistakes::compare).start();

		return "OK";
	}

	@GetMapping("right")
	public String right() {
		AtomicMistakes mistakes = new AtomicMistakes();
		new Thread(mistakes::add).start();
		new Thread(mistakes::compareRight).start();

		return "OK";
	}

	@GetMapping("wrong1")
	public Map<String, Integer> wrong1(@RequestParam(value = "count", defaultValue = "1000000") Integer count) {
		DataSyncMistakes.reset();

		IntStream.range(1, count).parallel().forEach(i -> new DataSyncMistakes().wrong());

		return ImmutableMap.of("result", DataSyncMistakes.getCounter());
	}

	@GetMapping("right1")
	public Map<String, Integer> right1(@RequestParam(value = "count", defaultValue = "1000000") Integer count) {
		DataSyncMistakes.reset();

		IntStream.range(1, count).parallel().forEach(i -> new DataSyncMistakes().right());

		return ImmutableMap.of("result", DataSyncMistakes.getCounter());
	}
}
