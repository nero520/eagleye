/**
* Common 使用 

  common.alert("警告信息",function(){
   //......
  });

common.success("警告信息",function(){
   //......
  });
  
common.error("警告信息",function(){
 //......
});  
common.frame("http://www.baidu.com","百度","800","600");

common.confirm("你确定要删除该信息吗?",function(result){
	if(result){
		alert("删除成功");
	}
});
//关闭
common.close()
*/
var Common=function Common(){};
//警告
Common.prototype.alert=function(message,callBack){
	jAlert(message,"警告",function(data){
	 		Common.MyCallBack(data,callBack);
	 });
};
/**
* 成功
*/
Common.prototype.success=function(message,callBack){
	jAlert(message,"成功",function(data){
	 		Common.MyCallBack(data,callBack);
	 });
};
/*
* 错误
*/
Common.prototype.error=function(message,callBack){
	jAlert(message,"错误",function(data){
	 		Common.MyCallBack(data,callBack);
	 });
};

/**
* 弹出新页面
*/
Common.prototype.frame=function(url,title,width,height){
	jIframe(url,title,width,height)
};
/**
* 确认框
*/
Common.prototype.confirm=function(message,callBack){
	jConfirm(message,"确认",function(data){
	 		Common.MyCallBack(data,callBack);
	 });
};

/**
* Prompt
*/
Common.prototype.prompt=function(message,title,value,callBack){
	jPrompt(message,title,value,function(data){
	 		Common.MyCallBack(data,callBack);
	 });
};
/**
* 提醒信息
*/
Common.prototype.message=function(message,callBack){
	jMessage(message,"提醒",callBack);
};

/**
 * html 片段 交互型
 */
Common.prototype.choose=function(html,title,width,height,callBack){
	jChoose(html,title,width,height,function(data){
	 		Common.MyCallBack(data,callBack);
	 });
}

/**
 * 
 * 
 关闭
*/
Common.prototype.close=function(){
$.alerts._hide();
};

/**
 关闭后新弹出提示信息
*/
Common.prototype.messageClose=function(message){
$.alerts._hide();
common.message(message);
};
/***
 打开新页签
*/
Common.prototype.openWin=function(url,title,loadOnce){
		loadOnce=loadOnce?"1":"0";
		window.top.right.testRight($('<a  dataType="iframe" dataLink="'+url+'" isRefresh="'+loadOnce+'">'+title+'</a>'));
};

Common.prototype.closeCurrentWin=function(){
	window.top.right.removeSelectedTab();
};

/**
* post发送信息
*/
Common.prototype.send=function(url,param,callBack){
	param=param||{};
	 Common.MyPost(url,param,function(data){
	 		Common.MyCallBack(data,callBack);
	 });
};

/*
* 将整个form表单动态提交
*/
Common.prototype.sendForm=function($form,callBack){
	var url=$form.attr("action");
	var param=$form.serialize();
	 Common.MyPost(url,param,function(data){
	 		Common.MyCallBack(data,callBack);
	 });
};

/** 私有方法不要调用*/
Common.MyCallBack=function(data,callBack){
	if(data && data.returnResult){
		
	}else{
		if(callBack){
	 		callBack(data);
		}
	}
};
Common.MyPost=function(url,param,callBack){
	//$("#back",window.top.right.document).show();
	$.ajax( {
				"url":  url,
				"data": param,
				"success": function(data){
						callBack(data);
						//$("#back",window.top.right.document).hide()
						},
				"dataType": "json",
				"cache": false,
				"type": "POST",
				"beforSend":function(){
					//$("#back",window.top.right.document).show();
				},
				"complete":function(){
					//$("#back",window.top.right.document).hide();
				},
				"error": function (xhr, error, thrown) {
					console.log(error);
					//$("#back",window.top.right.document).hide();
				}
			} );
};

var common=new Common();


//日期控件 My97Date重写
//eg: from:<input type="text"   id="dateFrom"  class="Wdate" maxId="dateTo"   format="yyyy-MM-dd" />    format="yyyy-MM-dd HH:mm:ss"
//	  to:<input type="text"    id="dateTo" class="Wdate"    minId="dateFrom" format="yyyy-MM-dd"/>
$(function () {
	$(".Wdate,.WdateTime").live("click", function () {
		var self = $(this);
		var s = "\tWdatePicker({dateFmt:self.attr(\"format\")";
		if (self.attr("maxDate")) {
			s += ",maxDate:$(this).attr(\"maxDate\")";
		}
		if (self.attr("minDate")) {
			s += ",minDate:$(this).attr(\"minDate\")";
		}
		if (self.attr("minId")) {
			s += ",minDate:\"" + $("#" + self.attr("minId")).val() + "\"";
		}
		if (self.attr("maxId")) {
			s += ",maxDate:\"" + $("#" + self.attr("maxId")).val() + "\"";
		}
		s+=",onpicked:function(dp){$('#'+dp.el.id).trigger('change')}";
		s += "})";
		return eval(s);
	});
	
	$(".closed_dialog").click(function(){
		parent.common.close();
	});
});

/**
 实例 字典项 class 为 dict 代码为 dictCode='' 显示的名称 为 dictName 如果需要填充隐藏域 则 dictId
 选择完毕 触发 该字段的 myTree.change事件 自己处理;
 多选的添加属性 checkbox即可
 <input type="input" class="dict" dictCode="<%=DiseaseDict.DISEASE_CATEGORY%>" dictName="类型" dictId="dictId" checkbox/>
 <input type="input" id="dictId"/>
*/
$(function(){
	$(".dict").live("focus",function(){
			if(!$(this).attr("combogrid")){//动态添加combogrid方法
				$(this).attr("combogrid",true);
				$(this).combogrid({
						url: function(self){return webRoot+'/dict/dictManage/getDictAjaxData.html?dictCode='+self.attr("dictCode")},
						debug:false,
						okIcon:false,
						alternate:true,
						autoChoose:false,
						searchButton: false,
						resetButton: function(self){
										return self.attr("reset")=="true"?true:false;
									},
						resetFields:function(self){
										 	if(typeof self.attr("dictId")=="undefined")
												return null; 
											return ["#"+self.attr("dictId")];
										},
						searchReset:false,
				    	replaceNull: true,
			            colModel: function(self){
			            	if(typeof self.attr("checkbox")!="undefined"){
			            		return [
				            			{'columnName':'chekcbox','width':'30',"label":function(self){return '<input type="button" value="ok" class="combogridOk"/>'},'preProcess':function(){return "<input type='checkbox' class='combogridCheckbox' />"}},
				            			{'columnName':'dictName','width':'70','label':function(self){return self.attr("dictName")}}
			            				];
			            	}else{
			            		return [
				            			{'columnName':'dictName','width':'100','label':function(self){return self.attr("dictName")}}
			            				];
			            	}
			            },
						select: function( event, ui ) {
							var self = $(this);
							self.val(ui.item.dictName);
							var dictId=self.attr("dictId");
							dictId=dictId.replace(/\./g,"\\.");
							dictId=dictId.replace(/\[/g,"\\[");
							dictId=dictId.replace(/\]/g,"\\]");
							console.log(dictId);
							$("#"+dictId).val("["+ui.item.id+"]");
							$("#"+dictId).trigger("change");
							return false;
						},
						check:function(event,ui,self){
							 var itemName="";
							 var itemId="";
							 for(var i=0;i<ui.items.length;i++){
							 	var item=ui.items[i];
							 	itemName+=item.dictName+";";
							 	itemId+="["+item.id+"]";
							 }
							 if(ui.items.length>0){
							 	itemName=itemName.substring(0,itemName.length-1);
							 }
							 self.val(itemName);
							 var dictId=self.attr("dictId");
							 if(typeof dictId!="undefined"){
								dictId=dictId.replace(/\./g,"\\.");
								dictId=dictId.replace(/\[/g,"\\[");
								dictId=dictId.replace(/\]/g,"\\]");
							 	$("#"+dictId).val(itemId);
								$("#"+dictId).trigger("change");
							 }
							 
							$(".combogrid").empty().hide();
						}
					});	
			}
	});
	
	
	/**
	字典列表树  dicCode 为字典代码 dictId 为保存字典id的 id
	复选 加 checkbox即可
	  <input type="input" class="myTree" readonly value="" id="b2" dictCode="<%=DiseaseDict.DISEASE_CATEGORY%>" dictId="hideId"/>
 	  <input type="input" id="hideId"/>
 */
 /**
	$(".myTree").live("focus",function(){
		var self=$(this);
		if(!self.attr("myTree")){//动态添加myTree方法
			self.attr("myTree",true);
			self.myTree();
		}
	});
	*/
});
/*----------------------------------- 去掉button聚焦时候的虚框 ----------------------------------*/
$(function(){
	$("input[type='button']").each(function(i){
	  $(this).focus(function(){
	  		$(this).blur();
	  });
 	});
});

/*----------------------------------- 判断浏览器类型 ----------------------------------*/
var browserName = navigator.userAgent.toLowerCase();
var mybrowser = {
	version: (browserName.match(/.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/) || [0, '0'])[1],
	safari: /webkit/i.test(browserName) && !this.chrome,
	opera: /opera/i.test(browserName),
	firefox:/firefox/i.test(browserName),
	msie: /msie/i.test(browserName) && !/opera/.test(browserName),
	mozilla: /mozilla/i.test(browserName) && !/(compatible|webkit)/.test(browserName) && !this.chrome,
	chrome: /chrome/i.test(browserName) && /webkit/i.test(browserName) && /mozilla/i.test(browserName)
}
//检索输入框提示信息
function showDefault(){
var get_name="";
	$("input").each(function(){
	if(typeof($(this).get(0).attributes['defaultvalue'])!='undefined')
	{
		get_name=$(this).attr("name");
		
		if($.trim($(this).val())==""){
		$(this).next().html($(this).get(0).attributes['defaultvalue'].nodeValue);
		$(this).next().css("color","#666");
		}
		
		$(this).blur(function(){
			if($(this).val()==""){
				$(this).next().html($(this).get(0).attributes['defaultvalue'].nodeValue);
				$(this).next().css("color","#666");
			}
		})
		$(this).focus(function(){
			if($(this).next().html()==$(this).get(0).attributes['defaultvalue'].nodeValue){
				$(this).next().html("");				
			}
		})
		$(this).next().click(function(){
			$(this).html("");	
			$("input[name="+get_name+"]").focus();							   
		})
	}	
})
}

/*--------------截取字符串-------------------------*/ 

function _subStr(str,len){ 
	var temps=str+"";
	var tempArr=new Array();
	while(temps.indexOf("\r\n")>=0) temps=temps.replace("\r\n"," ");
	while(temps.indexOf("\n")>=0) temps=temps.replace("\n"," ");
	while(temps.indexOf("  ")>=0) temps=temps.replace("  "," ");
	if(temps!=null && temps!="" && temps.indexOf(" ")>=0){
	    tempArr=temps.split(" ");
		temps="";
		for(var loop=0; loop<tempArr.length; loop++){
		     temps+=tempArr[loop]+"&nbsp;";
		}
	} 
	var reg = "[^\x00-\xff]";
	var re = new RegExp(reg);
	if(re.test(str)){
		if(str.length>(len/2))
		{	
			return "<span>"+str.substring(0,len/2)+"...</span>";
		}else
		{
			return str;
		}
	}else{
		if(str.length>(len))
		{	
			return "<span >"+str.substring(0,len-4)+"...</span>";
		}else
		{
			return str;
		}
	}
	
}



/**

<a href='http://www.baidu.com/a.png"' class='tooltip' target='_blank'><img/></a>

//js调用
$("a.tooltip").myTooltip();
*/
(function($){
        	$.fn.extend({
        		tooltip:function(options){
        			options=$.extend({
					src:"",
					width:130,
					imgHeight:350,
					imgWidth:350,
					index:0
				},options);
				var _self=this;
				_self.hide();
				//防止空链接导致退出
				if(options.src.indexOf(".")==-1)return;
				var img=new Image();
				$(img).load(function(){
					_self.attr("src",options.src);
					var x=img.width*100/options.width;
					if(x>100){
						_self.width(options.width);
					}
						var imgWidth=options.imgWidth;
				 	 	var imgHeight=options.imgWidth;
					/**
					   if(imgHeight>options.imgHeight){
					   	imgWidth=imgWidth*options.imgHeight/imgHeight;
					   	imgHeight=options.imgHeight;
					   }else if(imgWidth>options.imgWidth){
					   	imgHeight=imgHeight*options.imgWidth/imgWidth;
					   	imgWidth=options.imgWidth;
					   }
					   */
					   _self.attr("imgWidth",imgWidth);
					   _self.attr("imgHeight",imgHeight);
					_self.show();
				}).attr("src",options.src);
					return _self;
        		},
        		
        		myTooltip:function(options){
        			options=$.extend({
        				x:20,
        				y:30
					},options);
					
					$(this).each(function(index,self){
						self=$(self);
						$("img",self).tooltip({"src":self.attr("href")+"_small.png","width":130});
						var x=options.x;
						var y=options.y;
						//console.log(self.attr("href"));
						self.bind("mouseover",function(e){
								$("#tooltip").remove();
								var size=$(parent.document).scrollTop()-$(parent.document).height()+$(document).height();
								var src=self.attr("href")+"_big.png";
							    var imgWidth=$("img",self).attr("imgWidth");
							    var imgHeight=$("img",self).attr("imgHeight");
								var tooltip="<div id='tooltip'><img src='"+src+"'  width='"+imgWidth+"'  height='"+imgHeight+"'/></div>";
								$("body").append(tooltip);
								var height=$("#tooltip").height();
								var top=e.pageY-height+y;
								if(top<size){
									top=y+size;
								}
								if(size<0&&top<y){
									top=y;
								}
								$("#tooltip").css({
									top:top+"px",
									left:(e.pageX+x)+"px",
									position:"absolute",
									border:"1px solid #ccc",
									background:"#f4f4f4",
									padding:"3px",
									display:"none",
									color:"#fff"
								}).show("fast");
							}).bind("mouseout",function(e){
								$("#tooltip").remove();
							}).bind("mousemove",function(e){
								var size=$(parent.document).scrollTop()-$(parent.document).height()+$(document).height();
								var height=$("#tooltip").height();
								var top=e.pageY-height+y;
								if(top<size){
									top=y+size;
								}
								if(size<0&&top<y){
									top=y;
								}
								$("#tooltip").css({
									top:top+"px",
									left:(e.pageX+x)+"px"
								});
							}).bind("click",function(e){
								window.open($(this).attr("href")+"_big.png");
								return false;
							});
					});
        		}
        	});
})(jQuery);





Date.prototype.format = function (format) { 
   	var o = { 
	   	"M+": this.getMonth() + 1, 
	   	"d+": this.getDate(), 
	   	"h+": this.getHours(), 
	   	"m+": this.getMinutes(), 
	   	"s+": this.getSeconds(), 
	   	"q+": Math.floor((this.getMonth() + 3) / 3), 
	   	"S": this.getMilliseconds() 
   	}; 
   	if (/(y+)/.test(format)) { 
   		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length)); 
   	} 
   	for (var k in o) { 
	   	if (new RegExp("(" + k + ")").test(format)) { 
	   		format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length)); 
	   	} 
   	} 
   	return format; 
 }; 


/** 
 *转换日期对象为日期字符串 
 * @param l long值 
 * @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss 
 * @return 符合要求的日期字符串 
 */ 
 function getFormatDate(date, pattern) { 
	    if (date == undefined) { 
	    date = new Date(); 
	    } 
	    if (pattern == undefined) { 
	    pattern = "yyyy-MM-dd hh:mm:ss"; 
	    } 
	    return date.format(pattern); 
 }

