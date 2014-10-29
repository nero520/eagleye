# eagleye-dubbo-client 是什么?
----
1. eagleye-dubbo-client 只是针对dubbo服务调用框架的客户端.

2. 为了将监控各个模块进行解耦, 整个监控都是基于日志的.

3. 如果是针对thrift的调用情况收集, 我们可以再接个客户端,eg: eagleye-thrift-client.

4. 不管各个服务直接是通过什么rpc解决方案, 我们只要最终生成的日志格式满足eagleye-trace-domain 中的格式就可以了.

5. 下面简单介绍一下 eagleye-dubbo-client里面主要都做了什么.

	* 实现了Dubbo用于拓展的Filter接口,该接口采用责任链模式, Dubbo大部分拓展功能都是通过实现该接口完成了.

	* 我们主要在拓展的EagleyeDubboFilter中记录了每一次Dubbo调用时的local,remote 的上下文信息. 通过Dubbo的Context 和自定义的 ThreadLocal 传递调用链信息.

	* 我们只是在每一个rpc调用结束时,记录一条日志,并不会对具体的rpc调用产生太多的性能损耗,我们采用slf4j的方式定义日志输出, 这样如果各个应用中运用了主流的日志输出实现(如:log4j), 则不需要进行额外的修改.

	* 我们通过收集调用信息,创建eagleye-trace-domain日志对象, 并生产json,最终输出到日志.

	* 具体调用信息进入到日志以后, 我们会通过flume进行日志收集, 然后存储到日志服务器中的elasticsearch服务器. elasticsearch强大的日志检索功能,可以帮助我们根据traceId,方法名称等调用信息,快速检索到某一个调用链.