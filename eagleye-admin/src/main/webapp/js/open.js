function openTabOnRight(url,title){
	var path = getContextPath();
	try{
		window.parent.testRight($('<a  dataType="iframe" dataLink="'+url+'">'+title+'</a>'));
		
	}catch(e){
		window.parent.parent.testRight($('<a dataType="iframe" dataLink="'+url+'">'+title+'</a>'));
		
	}
	
}

function getContextPath() {
    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0,index+1);
    return result;
}