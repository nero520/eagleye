/**
	字典列表树  dicCode 为字典代码 dictId 为保存字典id的 id
	diseaseCatalogName
	  <input type="input" class="myTree" readonly value="" id="b2" dictCode="<%=DiseaseDict.DISEASE_CATEGORY%>" dictId="hideId"/>
 	  <input type="input" id="hideId"/>
*/
(function($){
	$.fn.extend({
		myTree:function(options){
			var self=$(this);
				self.attr("readOnly",true);
			options=$.extend({
				id:self.attr("id"),
				checkbox:typeof self.attr("checkbox")!="undefined",
				asyn:false,
				dictCode:self.attr("dictCode"),
				url:"dict/dictManage/getDictAjaxData.html?dictCode=",
				memuContent:"menuContent_"+self.attr("id"),
				treeDemo:"treeDemo_"+self.attr("id"),
				initFlat:false,
				treeId:"treeId_"+self.attr("id"),
				setting_data_simpleData_enable:true,
				setting_data_simpleData_idKey:"id",
				setting_data_simpleData_pIdKey:"parentId",
				setting_data_simpleData_rootPId:"-1",
				setting_data_key_name:"dictName",
				setting_view_showLine:true,
				setting_view_showIcon:true,
				setting_view_selectedMulti:false,
				setting_view_expandSpeed:"normal",
				beforeClick:function(treeId, treeNode){
					if(options.checkbox){
						var zTree = $.fn.zTree.getZTreeObj(options.treeDemo);
						zTree.checkNode(treeNode, !treeNode.checked, null, true);
						return false;
					}
				},
				onClick:function(){//单选用
					var zTree = $.fn.zTree.getZTreeObj(options.treeDemo);
						var nodes = zTree.getSelectedNodes();
						nodes.sort(function compare(a,b){return a.id-b.id;});
						var dictId="",name="";
							name += nodes[0].dictName;
							dictId = nodes[0].id;
						if(nodes[0].level==0){
							dictId="";
						}
						self.val(name);
						$("#"+self.attr("dictId")).val(dictId);
						self.trigger("myTree.change");
					 	options.hideMenu();
				},
				onCheck:function(e, treeId, treeNode) {//复选用
					if(options.asyn){
						id = "["+treeNode.id+"]";
						var d = $("#"+self.attr("dictId")).val();
						var n = self.val();
						var names = n.split(";");
						//如果勾选
						if(treeNode.checked){
							if(d.indexOf(id)<0){
								names.push(treeNode.dictName);
								$("#"+self.attr("dictId")).val(d+id);
							}
						}else{//去掉勾选
							if(d.indexOf(id)>-1){
								d = d.replace(new RegExp("\\["+treeNode.id+"\\]",'g'),'');
								for(var i=0;i<names.length;i++){
									if(names[i]==treeNode.dictName){
										names.splice(i,1);
									}
								}
								$("#"+self.attr("dictId")).val(d);
							}
						}
						for(var i=0;i<names.length;i++){
							if(names[i]==''){
								names.splice(i,1);
							}
						}
						self.val(names.join(";"));
					}else{
						var zTree = $.fn.zTree.getZTreeObj(options.treeDemo);
						var nodes = zTree.getCheckedNodes(true);
						var dictId="",name="";
						for(var i=0;i<nodes.length;i++){
							name+=nodes[i].dictName+";";
							dictId+="["+nodes[i].id+"]";
						}
						if(nodes.length>1){
							name=name.substring(0,name.length-1);
						}
						self.val(name);
						$("#"+self.attr("dictId")).val(dictId);
					}
					
						self.trigger("myTree.change");
					
				},
				init:function(){
					var html='<div id="'+options.memuContent+'" class="menuContent" style="display:none; position: absolute;">';
						html+='	<ul id="'+options.treeDemo+'" class="ztree myZtree" style="margin-top:0; width:180px; height: 300px;"></ul>';
						html+='</div>';
						if($("#"+options.memuContent).length==0){
							$("body").append(html);
						}
						if(options.asyn){
							$.fn.zTree.init($("#"+options.treeDemo), options.setting());
						}else{
							common.send(webRoot+options.url+options.dictCode,{},function(data){
			 			       if(data!=null){
							      zNodes = data;
							       var zTree = $.fn.zTree.init($("#"+options.treeDemo), options.setting(),zNodes);
							       zTree.expandAll(true);
							       setTimeout(function(){
									      var hideId=$("#"+self.attr("dictId")).val();
									      if(hideId.length>2){
									      	hideId=hideId.substring(hideId.indexOf("[")+1,hideId.lastIndexOf("]"));
									      }
									      var list=hideId.split("][");
									      for(var i=0;i<list.length;i++){
									      var node=zTree.getNodeByParam("id",list[i],null);
									      	zTree.checkNode(node, true, false, true);
									      }
							       
							       },10);
						       }
				 	       });
						}
				
				},
				filter:function(treeId, parentNode, childNodes) {
					if (!childNodes) return null;
					for (var i=0, l=childNodes.length; i<l; i++) {
						//如果是根节点，则去掉复选框
						if(childNodes[i].parentId==0)childNodes[i].nocheck=true;
						//是否叶子节点
						if(childNodes[i].isParent==1)childNodes[i].isParent=true;
						if(childNodes[i].isParent==0)childNodes[i].isParent=false;
						//是否应该选上
						var d = $("#"+self.attr("dictId")).val();
						if(d.indexOf("["+childNodes[i].id+"]")>-1)childNodes[i].checked=true;
					}
					return childNodes;
				},
				showMenu:function(){
					if(!options.initFlag){
						options.initFlag=true;
						options.init();
					}
					$("body").unbind("mousedown",options.onBodyDown);
					$("#"+options.menuContent).hide();
					var offset=self.offset();
					$("#"+options.memuContent).css({
							left:offset.left+"px",
							top:offset.top+self.outerHeight()+"px"
							}).slideDown("fast");
					 $("body").bind("mousedown",options.onBodyDown);
				},
				hideMenu:function(){
					$(".menuContent").fadeOut("fast");
					$("body").unbind("mousedown",options.onBodyDown);
				},
				onBodyDown:function(event){
					if($(event.target).closest(".menuContent").length==0){
						options.hideMenu();
					}
				},
				setting:function(){
					var result= {	
							data: {
								simpleData: {
									enable: options.setting_data_simpleData_enable,
									idKey: options.setting_data_simpleData_idKey,
									pIdKey:options.setting_data_simpleData_pIdKey,
									rootPId:options.setting_data_simpleData_rootPId
								},
								key:{
									name:options.setting_data_key_name
								}
							},
							async: {
								enable: options.asyn,
								url:options.url,
								autoParam:["id"],
								dataType: "json",
								dataFilter: options.filter
							},
							view: {
								showLine:options.setting_view_showLine,
								showIcon:options.setting_view_showIcon,
								selectedMulti: options.setting_view_selectedMulti,
								expandSpeed: options.setting_view_expandSpeed
							},
							callback: {
								beforeClick: options.beforeClick,
								onClick: options.onClick,
								onCheck:options.onCheck
							}
						}
						if(typeof self.attr("checkbox")!="undefined"){
							result["check"]=
								{
									enable: true,
									chkboxType: { "Y" : "", "N" : "" }
								};
						}
						return result;
					}

			},options);
			var timeout;
			window.myTree=window.myTree?window.myTree:[];
			window.myTree[self.attr("id")]=options;
			$(self).bind("click",function(){
				clearTimeout(timeout);
				var self=$(this);
				timeout=setTimeout(function(){
					options=window.myTree[self.attr("id")];
					options.showMenu();
				},300);
			});
			$(self).bind("dblclick",function(){
				clearTimeout(timeout);
				var self=$(this);
				options=window.myTree[self.attr("id")];
				options.initFlag=false;
				options.showMenu();
			});
			return self;
		}
	});
})(jQuery);