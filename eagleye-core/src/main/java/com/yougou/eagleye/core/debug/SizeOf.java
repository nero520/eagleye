package com.yougou.eagleye.core.debug;

/**
 * 计算对象所占内存的大小
 * @author liuhongwei
 *
 */
public abstract class SizeOf {

	private final Runtime s_runtime = Runtime.getRuntime();

	/**
	 *
	 * 子类负责覆盖该方法以提供被测试类的实例
	 *
	 * @return 被测试类的实例
	 */
	protected abstract Object newInstance();

	/**
	 *
	 * 计算实例的大小（字节数）
	 *
	 * @return 实例所占内存的字节数
	 * @throws Exception
	 */
	public int size() throws Exception {

		// 垃圾回收
		runGC();

		// 提供尽可能多（10万）的实例以使计算结果更精确
		final int count = 100000;
		Object[] objects = new Object[count];

		// 实例化前堆已使用大小
		long heap1 = usedMemory();
		// 多实例化一个对象
		for (int i = -1; i < count; ++i) {
			Object object = null;

			// 实例化对象
			object = newInstance();

			if (i >= 0) {
				objects[i] = object;
			} else {
				// 释放第一个对象
				object = null;
				// 垃圾收集
				runGC();
				// 实例化之前堆已使用大小
				heap1 = usedMemory();
			}
		}

		runGC();
		// 实例化之后堆已使用大小
		long heap2 = usedMemory();
		final int size = Math.round(((float) (heap2 - heap1)) / count);

		// 释放内存
		for (int i = 0; i < count; ++i) {
			objects[i] = null;
		}
		objects = null;
		return size;
	}

	private void runGC() throws Exception {
		// 执行多次以使内存收集更有效
		for (int r = 0; r < 4; ++r) {
			_runGC();
		}
	}

	private void _runGC() throws Exception {
		long usedMem1 = usedMemory();
		long usedMem2 = Long.MAX_VALUE;
		for (int i = 0; (usedMem1 < usedMem2) && (i < 500); ++i) {
			s_runtime.runFinalization();
			s_runtime.gc();
			Thread.currentThread().yield();
			usedMem2 = usedMem1;
			usedMem1 = usedMemory();
		}
	}

	/**
	 *
	 * 堆中已使用内存
	 *
	 * @return 堆中已使用内存
	 */
	private long usedMemory() {
		return s_runtime.totalMemory() - s_runtime.freeMemory();
	}
}