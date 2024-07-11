# PiMonitor
PiMonitor is the curriculumn design of Software Engineering. A simple agent with server and web application. This agent can be deploy on every personal computer or server to monitor the usage of CPU, memory, network and so on. Please give me a star, I beg beg you 了.

# 使用方法
1. 部署server模块，需要部署数据库并配置环境变量，包括MySQL，Redis，Kafka, InfluxDB等。
2. 部署web模块，web模块是用next.js搭建的web app，用于展示监控数据。
3. 登陆网站，创建用户并获取token。
4. 下载agent模块或自行编译，配置token环境变量。
5. 在服务中引用pimetric模块，定义并导出metric数据。
