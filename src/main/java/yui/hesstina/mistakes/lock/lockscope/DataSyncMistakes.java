package yui.hesstina.mistakes.lock.lockscope;

import lombok.Getter;

/**
 * 对象同步问题
 *
 * @author liuyi
 * @date 2021/1/27 17:21
 * @since 1.0
 */
public class DataSyncMistakes {

	@Getter
	private static int counter = 0;

	private static Object lock = new Object();

	/**
	 * 重置静态变量
	 *
	 * @return 返回静态变量
	 */
	public static int reset() {
		counter = 0;
		return counter;
	}

	/**
	 * 错误改变静态变量
	 */
	public synchronized void wrong() {
		counter++;
	}

	/**
	 * 锁同一个对象改变静态变量
	 */
	public synchronized void right() {
		synchronized (lock) {
			counter++;
		}
	}

}
