function initializeDate(){
	//初始化时间控件
	var now = new Date() ;
	var nowYear = now.getFullYear() ; //年
	var nowMonth = now.getMonth()+1<10?"0"+(now.getMonth()+1):now.getMonth()+1 ; //月
	var nowDay = now.getDate()<10?"0"+now.getDate():now.getDate() ; //日期
	var nowDate = nowYear+"-"+nowMonth+"-"+nowDay;
	now.setDate(now.getDate()+1);//获取1天后的日期 
	var y = now.getFullYear(); 
	var m = (now.getMonth()+1)<10?"0"+(now.getMonth()+1):now.getMonth()+1 ; //月
	var d = now.getDate()<10?"0"+now.getDate():now.getDate() ; //日期
	var nowTime = y+"-"+m+"-"+d;
	
	$("#start_time").val(nowDate) ;
	$("#end_time").val(nowTime) ;
}

function initializeDateHi(){
	//初始化时间控件
	var now = new Date() ;
	$("#start_time").val(now.Format("yyyy-MM-dd")+' 00:00');
	now.setDate(now.getDate()+1);
	$("#end_time").val(now.Format("yyyy-MM-dd")+' 00:00') ;
}

function initializeLoginDate(){
	//初始化时间控件
	var now = new Date() ;
	$("#end_time").val(now.Format("yyyy-MM-dd hh:mm"));
	now.setHours(now.getHours()-1);
	$("#start_time").val(now.Format("yyyy-MM-dd hh:mm")) ;
}

Date.prototype.Format = function(fmt)   
{
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}  