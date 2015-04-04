package com.gbilet.service;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import com.gbilet.bean.UserBean;
import com.gbilet.entity.User;

public class DAO {
	
	public static EntityManager getEntityManager(){
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		UserBean userBean = (UserBean) sessionMap.get("user");
		EntityManager entityManager = userBean.getEntityManager();
		entityManager.clear();
		return entityManager;
	}
	
	public static User getCurrentUser(){
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		UserBean userBean = (UserBean) sessionMap.get("user");
		return userBean.getUser();
	}
	
}
