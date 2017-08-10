package cc.heroy.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
* @ClassName: ThreadPoolUtil
* @Description: 单例模式 ：自定义线程池的使用
* @author BeiwEi
* @date 2017年8月10日 下午3:12:57
*
 */
public class ThreadPoolUtil {
	
	
	private static ThreadPoolExecutor threadPool;
	
	static{
		/**
		 * 创建自定义线程池
		 * keepAliveTime:线程工作完后，等待下一任务时间，超过就关闭线程
		 * unit : 与上面配合使用，单位：分，秒
		 */
		threadPool = new TimingThreadPool(10,10,1,TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(10));
	}
	
	private ThreadPoolUtil(){
	}
	
	public static ThreadPoolExecutor getThreadPool(){
		return threadPool;
	}
}
