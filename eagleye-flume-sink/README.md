## 自定义应用日志收集, 并发往redis队列

### 一. 概述

> 将dubbo, jboss, tomcat 等应用中的日志进行收集.每个应用的日志产生端,都会部署一个flume-agent,具体如下:
1. source: exec tail
2. channel: file
3. sink: avro-rpc


> 日志接收端也会部署一个flume-agent,具体如下:
1. source: avro-rpc
2. channel: file
3. sink1: custom sink ;  sink2: hdfs

> 日志在收集过程中需要添加如下标签,便于对日志进行分类:
1. 应用所在机器的ip地址
2. 应用名称


### 二. 部署

1. 执行 sh bootstrap.sh 可直接将程序部署到flume的server端

注: 需要手动修改flume的server所在的服务器地址.