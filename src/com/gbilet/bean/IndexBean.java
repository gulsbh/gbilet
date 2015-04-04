package com.gbilet.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import com.gbilet.entity.Event;
import com.gbilet.service.DAO;

@ViewScoped
@ManagedBean(name="index", eager=true)
public class IndexBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private EntityManager entityManager;
	
	private List<Event> organizationList;
	
	@PostConstruct
	void initialiseSession() {
	    FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	}

	@SuppressWarnings("unchecked")
	public IndexBean() {
		entityManager = DAO.getEntityManager();
		organizationList = entityManager.createQuery("SELECT organization FROM GbOrganization organization ").getResultList();
	}

	public List<Event> getOrganizationList() {
		return organizationList;
	}

	public void setOrganizationList(List<Event> organizationList) {
		this.organizationList = organizationList;
	}
	
}
