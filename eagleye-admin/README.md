# 简述

----
eagleye-admin 主要可以优购网统一监控系统平台的web管理界面, 在这里面可以对收集的日志进行检索, 进行用户组,应用,预警规则的管理, 可以对dubbo调用的跟踪监控展示.

目录结构说明如下:
1. /doc 文件夹中放置了主要项目说明文档,部署文档, 数据库初始化脚本等.
2. /src/main/resources/spring 下放置所有和spring相关的配置文件.
3. /src/main/resources/config.properties 中记录了redis,elasticsearch,预警规则相关的配置,spring配置文件会引用这里面的参数. 默认生产环境会将该文件放置/etc/yougouconf/eagleye-admin/ 目录下, 开发环境,生产环境的读取路径,根据/src/main/resources/spring/dataAccessContext.xml中进行配置.
4. src/test/java 目录下放置了基本的单元测试样例.
5. 项目中引用的静态常量统一放在com.yougou.eagleye.admin.constants.AppConstants.java 中, 项目中不允许出现硬编码现象.
6. 所有controller中的方法只负责接参数,返回参数, 调用services中的方法. 具体的业务逻辑都写在services的方法中, 以最大保持事务的一致性. services的方法中最好不要超过50行. dao中的方法,只进行对单个领域模型的原子操作.
7. hibernate级联关系, 最好不要超过一级关联, 如果有多表关联,可以从设计上优化为一级关联.
8. /src/main/webapp/css or js or images 放置自定义的css,js,图片文件. plugins文件夹下放置引用的第三方插件.