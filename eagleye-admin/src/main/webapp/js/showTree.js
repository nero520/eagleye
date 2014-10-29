
(function($){
	$.fn.extend({
		showTree:function(options){
			var self=$(this);
			var $id=self.attr("id");
			options=$.extend({
						id:$id,
						menuContent:"menuContent",
						initFlag:false,
						treeId:"treeDemo", //treeid
						setting_data_simpleData_enable:true,
						setting_data_simpleData_idKey:"id",
						setting_data_simpleData_pIdKey:"parentId",
						setting_data_simpleData_rootPId:"-1",
						setting_data_key_name:"className",
						setting_view_showLine:true,
						setting_view_showIcon:true,
						setting_view_selectedMulti:false,
						setting_view_expandSpeed:"normal",
						beforeClick:function(){},
						onClick:function(){},
						init:function(){},
						showMenu:function() {
							if(!options.initFlag){
								options.initFlag=true;
								options.init();
							}
							$("body").unbind("mousedown", options.onBodyDown);
							$("#"+options.menuContent).hide();
							var cityObj = $("#"+options.id+"");
							var cityOffset = $("#"+options.id+"").offset();
							$("#"+options.menuContent).css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
							$("body").bind("mousedown", options.onBodyDown);
						},
						hideMenu:function() {
							$("#"+options.menuContent).fadeOut("fast");
							$("body").unbind("mousedown", options.onBodyDown);
						},
						onBodyDown:function(event) {
							if (!(event.target.id == "menuBtn" || event.target.id == options.id || event.target.id == options.menuContent || $(event.target).parents(options.menuContent).length>0)) {
								options.hideMenu();
							}
						},
						settings:{data:1}
					},options);
			
				 setting={
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
						view: {
							showLine:options.setting_view_showLine,
							showIcon:options.setting_view_showIcon,
							selectedMulti: options.setting_view_selectedMulti,
							expandSpeed: options.setting_view_expandSpeed
						},
						callback: {
							beforeClick: options.beforeClick,
							onClick: options.onClick
						}
					};
					hideMenu=function(){
						options.hideMenu();
					};
			
			self.live("click",function(){
				options.showMenu();
			});
		}
	})
})(jQuery);