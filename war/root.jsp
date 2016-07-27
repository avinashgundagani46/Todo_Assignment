
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.assignment.model.TaskModel"%>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Home</title>
</head>
<body>

	<%
	UserService userService = UserServiceFactory.getUserService();
	String url= "";
	String urlLinktext = "";
	User user = null;
	if(!userService.isUserLoggedIn()){
		url= userService.createLoginURL("\\");
		urlLinktext = "Login";
	}else{
		user = userService.getCurrentUser();
		url = userService.createLogoutURL("\\");
	    urlLinktext = "Logout";
	}
	    
	%>
	<div >
		<div style="margin-left: 10%; margin-top: 5%;"><a href = "<%= url %>"><%= urlLinktext %></a></div>
		<div style="margin-top: 10%; margin-left: 40%;">
			
				<%
				if (user != null){
					List<TaskModel> tasks = (List<TaskModel>) request.getSession().getAttribute("tasks");
					int index=0;
				%>
				
				<form action="/" method="post" onsubmit="return updateIndex()" id= "form1">
					<%
    					session.setAttribute("index", index);
        			%>
					<ul>
						<%
						for(TaskModel task : tasks) {
						%>
							<li>
							<label><%=task.getName()%> :: </label>
							<label>"<%= task.isCompleted()? "Completed": "Pending" %>"</label>
							<button type="submit" name = "show" onclick="clickedIndex = '<%=index%>';" class = "button">Edit</button>
							<input class = "button" onclick="clickedIndex = '<%=index%>';" type="submit" name = "delete" value="Delete">
							<input class = "button" onclick="clickedIndex = '<%=index%>';" type="submit" name = "toogleComplete" value="Toggle Complete">
							</li>
						<%
							index++;
						}
						%>
					</ul>
					<div>
						<a href="/add.jsp">Add a task</a>
					</div>
				<% 
				}
				%>
					
					<input type="hidden" value="" name = "index" id = "index">
				</form>
		</div>
	</div>
	<script type="text/javascript">
		function updateIndex(){
			var selectedIndex = this.clickedIndex;
			document.getElementById('index').value = selectedIndex;
			return true;
		}
	</script>
</body>
</html>