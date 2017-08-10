package cc.heroy.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import cc.heroy.bean.IP;
import cc.heroy.strategy.htmlAnalyzer.BaseHtmlAnalyzer;
import cc.heroy.thread.IPSpider;
import cc.heroy.util.HttpClientUtil;
import cc.heroy.util.ThreadPoolUtil;

/**
 * 
* @ClassName: IPtimer
* @Description: 定时任务，每15分钟从网站获取可用IP
* @author BeiwEi
* @date 2017年8月10日 下午2:15:00
*
 */
public class IPtimer {

	Logger logger = Logger.getLogger(this.getClass());
	
	Properties prop = new Properties();
	/**
	 * 
	* @Title: crawlerJob 
	* @Description: 具体实现 
	* @Author 彭俊豪
	 */
	@SuppressWarnings("unchecked")
	public void crawlerJob(){
//开始时间
long begin = System.currentTimeMillis();
		ConcurrentHashMap<String,Set<String>> entitys =new ConcurrentHashMap<>();
		BlockingQueue<IP> IPs = new ArrayBlockingQueue<>(120);
		CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();;
		List<String> list = new ArrayList<String>();
		//存储可用的IP
		Vector<IP> vector = new Vector<IP>();
//		CountDownLatch end = new CountDownLatch(1);
		

		//获得线程池
		ThreadPoolExecutor threadPool = ThreadPoolUtil.getThreadPool();
		//解析url.properties文件
		handleProp(entitys,list);
		CountDownLatch over = new CountDownLatch(list.size());
		
		//分发任务并执行
		for(String key : list){
			Set<String> urls = entitys.get(key);
			Class<BaseHtmlAnalyzer> clazz;
			try {
				clazz = (Class<BaseHtmlAnalyzer>) Class.forName(prop.getProperty(key));
				threadPool.execute(new IPSpider(urls,httpClient,IPs,clazz,vector,over));
			} catch (ClassNotFoundException e) {
System.out.println("类文件："+key+"未找到！");
				e.printStackTrace();
			}
			
		}
		try {
			over.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			
	for(IP ip : vector){
		System.out.println(ip);
	}
	threadPool.shutdown();
System.out.println(System.currentTimeMillis()-begin);
	}
	
	/**
	 * 
	* @Title: handleProp 
	* @Description: 解析properties文件，并将数据传入entitys中
	* @Author 彭俊豪
	 */
	private void handleProp(ConcurrentHashMap<String,Set<String>> entitys,List<String> list){
		// 加载url.properties文件
				try {
					prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("url.properties"));
					logger.info("正在加载url配置文件...");
System.out.println("正在加载url配置文件...");
				} catch (IOException e) {
					logger.error("加载url.properties文件失败");
					e.printStackTrace();
				}
				// 将KEY-VALUE依次取出来放入entitys
				Iterator<String> it = prop.stringPropertyNames().iterator();
				while (it.hasNext()) {
					String key = it.next();
					if (key.length() == 1) {
						if (!entitys.containsKey(key)) {
							entitys.put(key, new HashSet<String>());
						}
						list.add(key);
					} else {
						if (entitys.get(key.substring(0, 1)) == null) {
							entitys.put(key.substring(0, 1), new HashSet<String>());
						}
						entitys.get(key.substring(0, 1)).add(prop.getProperty(key));
						logger.info("导入: "+key+"="+prop.getProperty(key));
System.out.println("添加 --》" + key + prop.getProperty(key));
					}
				}
	}
}
