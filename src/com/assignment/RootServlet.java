package com.assignment;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.assignment.model.TaskModel;
import com.assignment.model.User;
import com.assignment.utils.Common;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Servlet implementation class RootServlet
 */
public class RootServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RootServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("In Get");
        String thisURL = request.getRequestURI();
        System.out.println(thisURL);
        if (request.getUserPrincipal() != null) {
        	
        	Key k = Common.getUserKey(Common.getUser());
        	hasUserLoggedIn(k);
        	
        	List<TaskModel> tasks = getTasks(k);
        	System.out.println("Number of tasks: "+ tasks.size());
        	request.getSession().setAttribute("tasks", tasks);
        	
        	response.sendRedirect("/root.jsp");
        }else{
        	getServletContext().getRequestDispatcher("/root.jsp").forward(request,response);
        }

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String thisURL = request.getRequestURI();
        
        if (request.getUserPrincipal() != null) {
        	if(request.getParameter("show")!=null){
        		int index = Integer.parseInt(request.getParameter("index"));
        		showTask(Common.getUserKey(Common.getUser()), index, request, response);
        	}else if(request.getParameter("delete")!=null){
        		int index = Integer.parseInt(request.getParameter("index"));
        		deleteTask(Common.getUserKey(Common.getUser()), index);
        		response.sendRedirect("/");
        	}else if(request.getParameter("update")!=null){
        		int index = Integer.parseInt(request.getParameter("index"));
        		String taskname = request.getParameter("taskname");
        		Date date = new Date();
        		updateTask(Common.getUserKey(Common.getUser()), index, taskname, date);
        		response.sendRedirect("/");
        	}else if(request.getParameter("toogleComplete")!=null){
        		int index = Integer.parseInt(request.getParameter("index"));
        		toggleTask(Common.getUserKey(Common.getUser()), index);
        		response.sendRedirect("/");
        	}
        }else{
        	
        	response.sendRedirect("/");
        }
	}
	
	static void deleteTask(final Key k, final int index){
		PersistenceManager pm = JDOHelper.getPersistenceManagerFactory("transactions-optional").getPersistenceManager();
		pm.currentTransaction().begin();
		User user = pm.getObjectById(User.class, k);
		TaskModel taskDeleted = user.getTask(index);
		user.deleteTask(index);
		pm.deletePersistent(taskDeleted);
		pm.currentTransaction().commit();
		pm.close();
	}
	
	static void toggleTask(final Key k, final int index){
		PersistenceManager pm = JDOHelper.getPersistenceManagerFactory("transactions-optional").getPersistenceManager();
		pm.currentTransaction().begin();
		User user = pm.getObjectById(User.class, k);
		TaskModel taskModified = user.getTask(index);
		taskModified.setCompleted(!taskModified.isCompleted());
		pm.makePersistentAll(taskModified);
		pm.currentTransaction().commit();
		pm.close();
	}
	
	static void updateTask(final Key k, final int index, String taskName, Date date){
		PersistenceManager pm = JDOHelper.getPersistenceManagerFactory("transactions-optional").getPersistenceManager();
		pm.currentTransaction().begin();
		User user = pm.getObjectById(User.class, k);
		TaskModel taskModified = user.getTask(index);
		taskModified.setCreatedDate(date);
		taskModified.setName(taskName);
		pm.makePersistentAll(taskModified);
		pm.currentTransaction().commit();
		pm.close();
	}
	
	static void showTask(final Key k, final int index, HttpServletRequest req, HttpServletResponse res){
		PersistenceManager pm = JDOHelper.getPersistenceManagerFactory("transactions-optional").getPersistenceManager();
		pm.currentTransaction().begin();
		User user = pm.getObjectById(User.class, k);
		pm.currentTransaction().commit();
		TaskModel task = user.getTask(index);
		pm.close();
		req.getSession().setAttribute("task", task);
		req.getSession().setAttribute("index", index);
		try {
			res.sendRedirect("/show.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void hasUserLoggedIn(final Key k){
		PersistenceManager pm = JDOHelper.getPersistenceManagerFactory("transactions-optional").getPersistenceManager();
		
		Query q = pm.newQuery("select id from " + User.class.getName());
		List<Key> ids = (List<Key>) q.execute();
		
		System.out.println("Size : " +ids.size());
		
		if(!ids.contains(k)){
			User user = new User(Common.getUserKey(Common.getUser()));
			pm.makePersistent(user);
		}else{
			for(Key id : ids)
				System.out.println(id.getKind());
		}
		pm.close();
	}
	
	static List<TaskModel> getTasks(final Key k){
		
		PersistenceManager pm = JDOHelper.getPersistenceManagerFactory("transactions-optional").getPersistenceManager();
		
		User user = pm.getObjectById(User.class, k);
		
		return user.getTasks();
	}
}
