# proxy_ip（IP扫描系统）
起因：
------
        在项目[豆瓣爬虫](https://github.com/Alooooha/DoubanSpider)中，因为频繁访问豆瓣，导致经常被网站封IP，所以决定写个WEB项目，定时从HTTP代理网站中爬取免费的代理IP。


功能：
------
<br>①项目挂载到远程服务器，每隔15分钟从网上HTTP代理网站爬取代理IP，提取有用IP，存储到redis中
<br>②提供一个GET接口，可获取10个可使用IP

update (17-8-9)
------
<br>①搭建maven项目proxy_ip
<br>②使用Springmvc框架
<br>③使用策略模式和反射机制解决从不同网站解析ip信息的方式不同带来的问题

<br>--添加BaseHtmlAnalyzer作为Ip181Daili，Ip66Daili，XiciDaili等类的父接口，实现不同解析html源码的策略
<br>--创建HTMLAnalyzer线程类，并在analyzer方法中接收不同的class对象，实例化对象执行html解析
<br>--创建IPSpider线程类，并在run()中加入HTMLAnalyzer线程

><br>问题：
<br>①使用[ip66代理测试接口](http://www.66ip.cn/yz/post.php)测试代理IP时发现 无高匿ip，少量透明ip和大多数无效ip
<br>②测试代理ip时使用单线程，效率较低
<br>③请求url失败后抛出异常，导致程序阻塞
<br>④未添加日志功能


update (17-8-10)
------
<br>--添加log4j日志功能
<br>--添加IPTester线程类，用于测试代理ip是否可用
<br>--自定义线程池类TimingThreadPool继承ThreadPoolExecutor，添加日志记录功能
<br>--添加ThreadPoolUtil工具类
><br>描述：
<br>Ⅰ.今天使用[seofangfa](https://seofangfa.com/checkproxy/)测试时结果有小部分ip是高匿的，为了后续的开发，暂时以该网站作为测试工具！
<br>Ⅱ.遇见一个问题：3个多线程进行测试ip时，发现线程测试43个ip花了大约4分钟，一开始我以为是逻辑问题，后面
     <br>被我排除。我设置httpclient请求连接时间和连接超时为2秒，没有效果，最后我分析报错，发现原来是httpclient重试
     <br>机制的锅，它默认重复请求3次！修改方法：[httpclient文档](http://hc.apache.org/httpcomponents-client-4.5.x/tutorial/html/index.html)1.5.4节。

update (17-9-15)
------
><br>使用IPTestTask任务对从网站上爬取的IP进行可用性测试时，测试线程在获取响应时遇见socket阻塞，导致线程无法进行工作,也无法关闭，如果不解决这个问题，而将项目放在服务器上，周期性执行爬虫任务，会引起阻塞线程累积，JVM内存不断减少，最终引起服务器崩溃。

<br>我能想到的解决方案：
<br>方案Ⅰ
<br>         在发起HttpGet请求时HttpClient允许设置RequestConfig，在RequestConfig中有以下几个方法：
<br>setSocketTimeout()：请求获取数据的超时时间，如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
<br>setConnectTimeout()：设置连接超时时间。
<br>setConnectionRequestTimeout()：设置从connect Manager获取Connection 超时时间。
<br>我将他们分别设置为5000毫秒，经过多次测试，失败！
<br>后来我使用DEBUG模式，并在IP测试线程均阻塞的情况下，暂停其中一个，在其栈区最上面的栈帧中找到了答案：
![image]()
<br>点击找到socketRead0()，发现这是个native方法，也就是说该方法不是由JAVA写的，JAVA代码对它无法产生影响。
![image]()
<br>
<br>方案Ⅱ
<br>          既然我无法阻止socket阻塞，那我把线程关掉总行吧。
<br>首先我想到了线程自带的interrupt()方法，我在网上查看了interrupt()详解，发现当线程调用sleep()，wait()，join()进行阻塞状态，可通过interrupt()将线程中断，失败！
<br>
<br>
