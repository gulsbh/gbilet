package com.gbilet.bean;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.gbilet.entity.Event;
import com.gbilet.service.DAO;

@ViewScoped
@ManagedBean(name="organizationDetailC", eager=true)
public class OrganizationDetailCBean {
	
	private EntityManager entityManager;
	
	private Event organization;
	private boolean canBuy = true;
	private String id;
	
	@PostConstruct
	private void initialiseSession() {
	    FacesContext.getCurrentInstance().getExternalContext().getSession(true);	    
	}
	
	public OrganizationDetailCBean(){
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    id = request.getParameter("id");
	    
	    entityManager = DAO.getEntityManager();
        
	    if(id!=null){
	        int idNumber = Integer.parseInt(id);
	        organization = (Event)entityManager.createQuery("SELECT organization FROM GbOrganization organization " +
	        							"WHERE organization.id=:id")
	        							.setParameter("id", idNumber).getSingleResult();
	        
	        if(organization.getDate().before(Calendar.getInstance().getTime()))
	        	canBuy = false;
	    }
        
	}
	
	public String getFormattedDate(){
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy EE HH:mm");
		return formatter.format(organization.getDate());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Event getOrganization() {
		return organization;
	}

	public void setOrganization(Event organization) {
		this.organization = organization;
	}

	public boolean isCanBuy() {
		return canBuy;
	}

	public void setCanBuy(boolean canBuy) {
		this.canBuy = canBuy;
	}

}
