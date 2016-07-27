package com.assignment;

import java.io.IOException;
import java.text.ParseException;
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

/**
 * Servlet implementation class AddServlet
 */

public class AddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (Common.getUser() == null)
			response.sendRedirect("/");
		else {
			response.sendRedirect("/add.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String taskname = request.getParameter("taskname");
		
		Date date = new Date();

		PersistenceManager pm = JDOHelper.getPersistenceManagerFactory(
				"transactions-optional").getPersistenceManager();
		User user = pm.getObjectById(User.class, Common.getUser()
				.getUserId());
		try {
			user.addTask(taskname, date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		pm.close();
		response.sendRedirect("/");

	}
}
