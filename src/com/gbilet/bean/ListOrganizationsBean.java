package com.gbilet.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import com.gbilet.entity.Event;
import com.gbilet.entity.User;
import com.gbilet.service.DAO;

@ViewScoped
@ManagedBean(name="listOrganizations", eager=true)
public class ListOrganizationsBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private EntityManager entityManager;
	
	private List<Event> organizationList;
	
	@PostConstruct
	void initialiseSession() {
	    FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	}

	@SuppressWarnings("unchecked")
	public ListOrganizationsBean(){	
		try{
			entityManager = DAO.getEntityManager();
	        User user = DAO.getCurrentUser();
	        
	        if(user.getRole().equals("O"))
	        	organizationList = entityManager.createQuery("SELECT organization FROM GbOrganization organization " +
									"WHERE organization.gbUser=:user")
									.setParameter("user", user).getResultList();
	        else if(user.getRole().equals("S"))
	        	organizationList = entityManager.createQuery("SELECT organization FROM GbOrganization organization").getResultList();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public List<Event> getOrganizationList() {
		return organizationList;
	}

	public void setOrganizationList(List<Event> organizationList) {
		this.organizationList = organizationList;
	}
	
}
