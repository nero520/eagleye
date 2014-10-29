# 介绍

----

记录调用信息的纯POJO, 没有任何侵入性

‘’
	/**
	 * 跟踪调用日志标识
	 */
	private String traceLogFlag = "EAGLEYETRACELOG";

	/**
	 * 跟踪id, 唯一标识一个调用链集合
	 */
	private String traceId = "";
	

	/**
	 * 第一调用的id, 唯一标识一次dubbo调用
	 */
	private String rpcId = "";
	
	/**
	 * 父调用rpcId
	 */
	private String parentRpcId = "";
	
	/**
	 * 本身的应用名
	 */
	private String application = "";
	
	/**
	 * 调用的接口名称
	 */
	private String invokeInterface = "";
	
	/**
	 * 调用的接口中方法名称
	 */
	private String invokeMethod = "";
	
	/**
	 * 调用起始时间点
	 */
	private String startTime = "";
	
	/**
	 * 调用结束时间点
	 */
	private String endTime = "";
	
	/**
	 * 调用花费的时间, 单位ms
	 */
	private String spendTime = "";
	
	/**
	 * 调用IP
	 */
	private String rpcIp = "";
	
	/**
	 * 本地Ip
	 */
	private String localIp = "";
	
	
	/**
	 * 在一个调用链中的调用次序
	 */
	private String rpcIndex = "";
	
	/**
	 * 是否是提供者, 叶子节点 0:不是 1:是
	 */
	private String isProvider = "0";