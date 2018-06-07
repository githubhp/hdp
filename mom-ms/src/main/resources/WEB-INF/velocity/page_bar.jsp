<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!--  
##	PageBarII 翻页栏（可以放到网页任意位置）
##	但必须：
##	1、传入$form_name指定Form名称。
##	2、将以下三个隐藏字段复制到指定的Form表单里面。
## 	<input type="hidden" name="actionType" value="0" id ="actionType">
## 	<input type="hidden" name="pageNo" value="$pageBean.pageNo" id ="page">
## 	<input type="hidden" name="pageSize" value="$pageBean.pageSize" id ="pageSize">
##  3、传入$btn_query_page指定查询Button名称。
-->

<table style="width: 100%;">
	<tr>
		<td align="center">
			<!--显示 $pageBean.getStartNo() - $pageBean.getEndNo() 条--> 
			每页 
			<select name="setPageSize" onChange="chkSubmit(1);" id="setPageSize">
			  <option value ="10" 
			  	<c:if test="${pageBean.pageSize == 10}"> selected </c:if>>10</option>
			  <option value ="20"
			  	<c:if test="${pageBean.pageSize == 20}"> selected </c:if>>20</option>
			  <option value="30" 
			  	<c:if test="${pageBean.pageSize == 30}"> selected </c:if>>30</option>
			  <option value="50"
			  	<c:if test="${pageBean.pageSize == 50}"> selected </c:if>>50</option>
			</select>
			
			条共${pageBean.count}条 
			<c:choose>
				<c:when test="${pageBean.pageNo > 1 }">
					<span style="cursor: pointer; color: #039" onclick="JavaScript:chkSubmit(1);">首页</span> 
					<span style="cursor: pointer; color: #039" onclick="JavaScript:chkSubmit(2);">上一页</span> 
				</c:when>
				<c:otherwise>
					<span>首页</span>
					<span>上一页</span> 
				</c:otherwise>
			</c:choose>
			
			<c:choose>
				<c:when test="${pageBean.pageNo < pageBean.getPages() }">
					<span style="cursor: pointer; color: #039" onclick="JavaScript:chkSubmit(3);">下一页</span> 
					<span style="cursor: pointer; color: #039" onclick="JavaScript:chkSubmit(4);">末页</span> 
				</c:when>
				<c:otherwise>
					<span>下一页</span>
					<span>末页</span> 
				</c:otherwise>
			</c:choose>
			
			第${pageBean.pageNo }/${pageBean.getPages() }页 转到第 
			<input type="text" name="gotoPage" size="3" maxlength="4" value="${pageBean.pageNo }" onKeyDown="if(event.keyCode==13){document.all('btn__page_jump').click();}" onkeyup="value=value.replace(/[^\d]/g,'')" onforminput="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" id="gotoPage"> 
			页 
			<input type="button" name="btn__page_jump" class="button3d" value="Go" onclick="JavaScript:chkSubmit(6);" id="btn__page_jump">
		</td>
	</tr>
</table>
<script type="text/javascript">
function chkSubmit (actionType){
	var f = document.forms["<%=request.getParameter("form_name")%>"];
	if(f==null){
		alert("查询表单不存在！");
		return false;
	}
	var pageS = f.pageSize;
	if (pageS == null) {
		alert("你的查询表单里没有一个hidden字段pageSize");
		return false;
	}
	var ps = document.getElementById("setPageSize");
	for(var i=0;i<ps.length;i++){
		if(ps.options[i].selected) {
			pageS.value = ps.options[i].value;
			//alert(">>>>"+ps.options[i].value);
		}
	}
	var pageItem = f.actionType;
	if (pageItem == null) {
		alert("你的查询表单里没有一个hidden字段actionType");
		return false;
	}
	var page = f.pageNo;
	//alert(">>>>"+page.value);
	if (page == null) {
		alert("你的查询表单里没有一个hidden字段pageNo");
		return false;
	}
	pageItem.value = actionType;
	//alert(">>>>"+pageItem.value);
	if(pageItem.value==6){
		if(document.getElementsByName("gotoPage")[0].value==""){
			alert("跳转页数不能为空！");
			document.getElementsByName("gotoPage")[0].focus();
			return false;
		}else if(document.getElementsByName("gotoPage")[0].value > ${pageBean.getPages()}){
			alert("跳转页数不能大于总页数！");
			document.getElementsByName("gotoPage")[0].focus();
			return false;
		}else{
			page.value = document.getElementsByName("gotoPage")[0].value;
		}
	}
	
	//f.submit();	
	document.getElementById("<%=request.getParameter("btn_query_page")%>").click();
	//alert(11);
	
}
</script>
