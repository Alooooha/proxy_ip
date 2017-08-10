package cc.heroy.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * 
* @ClassName: TimingThreadPool
* @Description: 自定义线程池，添加日志功能
* @author BeiwEi
* @date 2017年8月10日 下午3:16:49
*
 */
public class TimingThreadPool extends ThreadPoolExecutor{

	public Logger logger = Logger.getLogger(this.getClass());
	
	public TimingThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		logger.info("启动线程:"+r.getClass().getSimpleName());
		super.afterExecute(r, t);
	}

	@Override
	protected void terminated() {
		logger.info("线程终止");
		super.terminated();
	}
}
