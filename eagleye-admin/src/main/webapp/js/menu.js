$(document).ready(function(){
	//页面中的DOM已经装载完成时，执行的代码
	$(".main > a").click(function(){
		//找到主菜单项对应的子菜单项
		var ulNode = $(this).next("ul");
		/*
		if (ulNode.css("display") == "none") {
			ulNode.css("display","block");
		} else {
			ulNode.css("display","none");
		}
		*/
		//ulNode.show("slow");//normal fast
		//ulNode.hide();
		//ulNode.toggle();
		//
		//ulNode.slideDown("slow");
		//ulNode.slideUp;
		ulNode.slideToggle();
		changeIcon($(this));
	});
	$(".hmain").hover(function(){
		$(this).children("ul").slideDown();
		changeIcon($(this).children("a"));
	},function(){
		$(this).children("ul").slideUp();
		changeIcon($(this).children("a"));
	});
});

/**
 * 修改主菜单的指示图标
 */
function changeIcon(mainNode) {
	if (mainNode) {
		if (mainNode.css("background-image").indexOf("collapsed.gif") >= 0) {
			mainNode.css("background-image","url('images/expanded.gif')");
		} else {
			mainNode.css("background-image","url('images/collapsed.gif')");
		}
	}
}
