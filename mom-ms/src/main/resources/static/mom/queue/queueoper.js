$(function () {
    var oTable = new TableInit();
    oTable.Init();
    $('#queuelist').on('check.bs.table uncheck.bs.table ' +
            'check-all.bs.table uncheck-all.bs.table', function () {
        $('#remove').prop('disabled', !$('#queuelist').bootstrapTable('getSelections').length);
        $('#modify').prop('disabled', !$('#queuelist').bootstrapTable('getSelections').length);
    });
});
var columns = [{
    checkbox : true
},{
    field: 'id',
    title: 'id',
    align:'left',
    halign:'center',
    visible:false
},{
    field: 'name',
    title: '队列名',
    align:'left',
    halign:'center',
    visible:false
},{
    field: 'content_format',
    title: '内容格式',
    align:'left',
    halign:'center',
    formatter : function(value,row,index) {
    	var retVal = "" ;
    	if(value == 1){
    		retVal = "JSON";
    	}else if(value == 2){
    		retVal = "CSV逗号分割";
    	}else if(vallue == 3){
    		retVal = "CSV制表符分割";
    	}else{
    		retVal = "异常格式";
    	}
    	return retVal;
	}
},{
    field: 'momTypes',
    title: '中间件',
    align:'left',
    halign:'center',
    formatter : function(value,row,index) {
    	var retVal = "" ;
    	if(value == 1){
    		retVal = "KAFKA";
    	}else if(value == 2){
    		retVal = "REDIS";
    	}else if(vallue == 4){
    		retVal = "KAFKA2";
    	}else{
    		retVal = "异常";
    	}
    	return retVal;
	}
}, {
    field: 'resultStatuss',
    title: '调试',
    align:'left',
    halign:'center',
    formatter : function(value,row,index) {
    	var retVal = "" ;
    	if(value == 1){
    		retVal = "是";
    	}else if(value == 2){
    		retVal = "否";
    	}else{
    		retVal = "异常";
    	}
    	return retVal;
	}
},];


function search(){
	var display = $('#querydiv').css("display");
	if(display!='none'){
		$('#querydiv').css("display",'none');
		$('#name').val('');
	}else{
		$('#querydiv').css("display",'block');
	}
}
function queryinit(){
	$('#querydiv').bootstrapTable('refresh');
};
var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#queuelist').bootstrapTable({
            url: root+'/sys/user/list',         //请求后台的URL（*）
			method : 'post', // 请求方式（*）
			toolbar : '#toolbar', // 工具按钮用哪个容器
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable : true, // 是否启用排序
//			sortName:"",
//			sortOrder : "loginName", // 排序方式
			singleSelect : true,
			pagination : true, // 是否显示分页（*）
			queryParams : oTableInit.queryParams,// 传递参数（*）
			queryParamsType : "limit", // 参数格式,发送标准的RESTFul类型的参数请求
			sidePagination : "server", // 分页方式：client客户端分页，server服务端分页（*）
			pageNumber : 1, // 初始化加载第一页，默认第一页
			pageSize : 10, // 每页的记录行数（*）
			pageList : [ 10, 25, 50, 100 ], // 可供选择的每页的行数（*）
//			search : true, // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
//			strictSearch : true,
			showColumns : true, // 是否显示所有的列
			showRefresh : true, // 是否显示刷新按钮
			minimumCountColumns : 2, // 最少允许的列数
			clickToSelect : false, // 是否启用点击选中行
			// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId : "id", // 每一行的唯一标识，一般为主键列
			showToggle : true, // 是否显示详细视图和列表视图的切换按钮
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表             
            columns: columns,
            onLoadSuccess:function(data){
            	console.log(data);
            	if(data.success=="false"){
            		var message = data.message.global;
            		if(message!=null)
            			alert(message);
            	}
            }
        });
    };
    //
    oTableInit.queryParams = function (params) {
        var temp = {    
            pageNo:params.offset, 
            pageSize:params.limit,
            orderBy:(params.sort==undefined)?"loginName":parmams.sort+" "+params.order,
            name:$('#name').val(),
        };
        return temp;
    };
    return oTableInit;
};

function usergrant(id,name,no){
	localStorage.tranferId = id;
	window.location.href = path+"sys/usergrant.html";
}
function add(){
	window.location=path+"sys/useradd.html";
}
function modify(){
	var selData = $("#userlist").bootstrapTable('getSelections');
	if (selData == undefined || selData == "") {
		alert("请选择修改数据");
		return;
	}
	var id = $.map(selData, function(row) {
		return row.id;
	});
	localStorage.tranferId = selData[0].id;
	window.location.href = path+"sys/usermodify.html";
}

function remove() {
	var jsonObj = $("#userlist").bootstrapTable('getSelections');
	var id = $.map(jsonObj, function(row) {
		return row.id;
	});
	console.log(" User remove id is "+id);
	var json = {
		"id":jsonObj[0].id
	};
	
	$.ajax({
		type : 'post',
		url : root+'/sys/user/delete',
		timeout : 60000,
		dataType : 'json',
		data : json,
		/*contentType : "application/json;charset=UTF-8",
		data : JSON.stringify(json),*/
		success : function(ret) {
			if(ret.success=="true"){
				console.log(ret);
				var arr = new Array();
				arr.push(ret.result);
				$("#userlist").bootstrapTable('remove', {
					field : 'id',
					values : arr
				});
				$('#remove').prop('disabled', !$('#queuelist').bootstrapTable('getSelections').length);
				$('#modify').prop('disabled', !$('#queuelist').bootstrapTable('getSelections').length);
			}else{
				alert(JSON.stringify(ret.message));
			}
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(XMLHttpRequest.responseText);
		}
	});
};

function getRowInfo() {
    return $.map($('#queuelist').bootstrapTable('getSelections'), function (row) {
    	console.log("row is "+row.netName);
        return row;
    });
}
