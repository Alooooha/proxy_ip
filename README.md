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

<br>问题：
<br>①使用[ip66代理测试接口](http://www.66ip.cn/yz/post.php)测试代理IP时发现 无高匿ip，少量透明ip和大多数无效ip
<br>②测试代理ip时使用单线程，效率较低
<br>③请求url失败后抛出异常，导致程序阻塞
<br>④未添加日志功能
