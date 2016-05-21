<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IPC模拟器</title>
<script type="text/javascript" src="/js/jquery-1.11.3.min.js"></script>
</head>

<body>
	<c:if test="${message != null }">
		<h2><font color="red">${message }</font></h2>
	</c:if>
	<table id="table"  border="1" width="800">
		<tbody>
		<tr><td><div>&nbsp;<input type="button" id="btnNew" value="增加" /></div></td></tr>
		<c:forEach items="${list }" var="response">
			<tr>
				<td>
					<form action="${_basePath}/admin"  method="post" >
					<input  type="hidden" class="opt" name="opt" value="" /> 
					<input  type="hidden" name="id" value="${response.id}" />
					<div>  
						<div class="caption">
							<span>uri: <input type="text" name="uri" size="50" value="${response.uri}" /></span>
							<span>&nbsp; method 
								<select name="method">
									<option value="GET" <c:if test="${response.method == 'GET'}">selected</c:if>>GET</option>
									<option value="POST" <c:if test="${response.method == 'POST'}">selected</c:if>>POST</option>
									<option value="PUT" <c:if test="${response.method == 'PUT'}">selected</c:if>>PUT</option>
									<option value="DELETE" <c:if test="${response.method == 'DELETE'}">selected</c:if>>DELETE</option>
								</select>
							</span>
							 <span>&nbsp;<input type="button" class="btnUpdate" value="保存" /></span>
							 <span>&nbsp;<input type="button" class="btnDel" value="删除" /></span>
							 <span>&nbsp;<input type="button" class="btnToggle" value="显示" /></span>							 							 							 
						</div>
						<div class="content" style="border: 1px; display: none">
							<textarea name="content" rows="30" cols="100">${response.content }</textarea>
						</div>
					</div>
					</form>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	 
	<div id="template" style="display:none">
			<form action="${_basePath}/admin"  method="post" >
			<input  type="hidden" class="opt" name="opt" value="add" /> 
			<div>
				<div class="caption">
					<span>uri: <input type="text" name="uri" size="50" value="" /></span>
					<span>&nbsp; method 
						<select name="method">
							<option value="GET">GET</option>
							<option value="POST">POST</option>
							<option value="PUT">PUT</option>
							<option value="DELETE">DELETE</option>
						</select>
					</span>
					 <span>&nbsp;<input type="button" class="btnAdd" value="保存" /></span>
				</div>
				<div class="content" style="border: 1px;">
					<textarea name="content" rows="30" cols="100"></textarea>
				</div>
			</div>
			</form>
	</div>
</body> 
<script type="text/javascript">
	(function($) {
		$(".btnToggle").click(function() {
			var $content = $(this).closest("td").find(".content");
			var isHide = !$content.is(":visible");
			if(isHide) {
				$("table .content").hide();
				$(".btnToggle").val("显示");
			}
			$content.toggle();
			$(this).val($content.is(":visible") ? "隐藏" : "显示");
		});
		
		$(".btnUpdate").click(function(){
			var $form = $(this).closest("form");
			if(check($form)) {
				$form.find(".opt").val("update");
				$form.submit();
			}
		});
		
		$(".btnDel").click(function(){
			var $form = $(this).closest("form");
			$form.find(".opt").val("del");
			$form.submit();
		});
		
		$("#btnNew").click(function() {
			$("table .content").hide();
			$(".btnToggle").val("显示");
			$("table tbody").append("<tr><td>" + $("#template").html() + "</td></tr>");
			
			$(".btnAdd").click(function() {
				var $form = $(this).closest("form");
				if(check($form)) {
					$form.submit();
				}
			});
		});

		
		function check($form) {
			if($form.find("input[name='uri']").val().trim() == '') {
				alert("url为空");
				return false;
			}
			return true;
		}
	})(jQuery);
</script>
</html>