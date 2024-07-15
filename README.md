# PiMonitor
PiMonitor is the curriculumn design of Software Engineering. A simple agent with server and web application. This agent can be deployed on every personal computer or server to monitor the usage of CPU, memory, network and so on. Please give me a star, I beg beg you 了.

# 系统架构
系统主要分为四个模块：Metric, Agent, Server和Web。架构图如下：
![image-20240715220848894](https://vblog-1315512378.cos.ap-guangzhou.myqcloud.com/imgs/image-20240715220848894.png)

下面的文章介绍了本系统的设计思路和参考资料：

[如何在项目中开启上帝视角 —— 由浅入深研究监控系统](https://veni222987.github.io/2024/07/15/%E5%A6%82%E4%BD%95%E5%9C%A8%E9%A1%B9%E7%9B%AE%E4%B8%AD%E5%BC%80%E5%90%AF%E4%B8%8A%E5%B8%9D%E8%A7%86%E8%A7%92%E2%80%94%E2%80%94%E7%94%B1%E6%B5%85%E5%85%A5%E6%B7%B1%E7%A0%94%E7%A9%B6%E7%9B%91%E6%8E%A7%E7%B3%BB%E7%BB%9F/)

# 使用方法
1. 部署server模块，需要部署数据库并配置环境变量，包括MySQL，Redis，Kafka, InfluxDB等。
2. 部署web模块，web模块是用next.js搭建的web app，用于展示监控数据。
3. 登陆网站，创建用户并获取token。
4. 下载agent模块或自行编译，配置token环境变量。
5. 在服务中引用pimetric模块，定义并导出metric数据。