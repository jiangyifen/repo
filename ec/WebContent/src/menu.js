var iframeTop = 53;

function setIframeTop(){
    document.getElementsByTagName("iframe")[0].style.top = iframeTop;
}

function jump(iframe,tab){
	if(tab.className=="TabDsbl"){
		return;
	}else{
        var menu = document.getElementById("menu");
        var tds = menu.rows(0).cells;
        for(var i=0;i<tds.length;i++){
            if(tds[i].className!="NoTabs" && tds[i].className!="TabDsbl"){
                tds[i].className = "Tab";
            }
        }
        tab.className="TabSlct"; 
		
        var iframes = document.getElementsByTagName("iframe");
        var num = iframes.length;
        for(var i = 0; i < num; i++){
            iframes[i].style.top = -99999;
        }
        document.getElementById(iframe).style.top = iframeTop;
	}
}



function isChinese(str){
    var re = /[^\u4e00-\u9fa5]/; 
    if(re.test(str))
        return false; 
    return true; 
} 
