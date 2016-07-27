package com.assignment.utils;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class Common {
	public static User getUser(){
		
		UserService userservice = UserServiceFactory.getUserService();
		
		return userservice.getCurrentUser();
	}
	
	public static Key getUserKey(final User u){
		
		Key k = KeyFactory.createKey(User.class.getSimpleName(), u.getUserId());
		
		return k;
	}
}
