
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.assignment.model.TaskModel"%>
<%@page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>View Task</title>
</head>
<body>
	<div style="margin-left: 10%; margin-top: 5%">
		<a href="/">Go Back</a>
	</div>
	<%
		TaskModel task = (TaskModel) request.getSession().getAttribute("task");
		int index = (Integer) request.getSession().getAttribute("index");
		/* = (Integer)request.getSession().getAttribute("index") */ 
	%>
	<form action="/" method="post">
		<div style="margin-left: 30%; margin-top: 10%">
			<label>Name : </label><input type="text" name= "taskname" value="<%=task.getName()%>">
			<label>Creation Date: </label><label><%=task.getCreatedDate()%></label>
			<input type="hidden" name = "index" id = "index" value="<%=index%>">
		</div>
		<div style="margin-left: 40%; margin-top: 15%">
			<button type="submit" name = "update">Save</button>
		</div>
	</form>
</body>
</html>