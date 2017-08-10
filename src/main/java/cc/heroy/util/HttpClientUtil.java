package cc.heroy.util;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * 单例模式，生成单个 HttpClient
 *
 */
public class HttpClientUtil {
	//连接池最大连接数
//	private static final int maxSize = 50 ;
	
	
	private static BasicCookieStore cookieStore ;
	private static CloseableHttpClient httpClient ;
	//HttpClient连接池
	private static PoolingHttpClientConnectionManager cm ;
	
	
	static{
//		cookieStore = new BasicCookieStore();
//		clientPool.setMaxTotal(20);
//		httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).setConnectionManager(clientPool).build();
		DefaultHttpRequestRetryHandler dhr = new DefaultHttpRequestRetryHandler(1, true);//设置超时重试次数为1  
		
		cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(20);
		httpClient = HttpClients.custom().setConnectionManager(cm).setRetryHandler(dhr).build();
	}

	//禁止构造器生成对象
	private HttpClientUtil(){
	}
	
	/**
	 * 获取HttpClient（单例）
	 */
	public static CloseableHttpClient getHttpClient(){
		return httpClient ;
	}
	
	/**
	 * 获取cookieStore（单例）
	 */
	public static CookieStore getcookieStore(){
		return cookieStore;
	}
	
}
