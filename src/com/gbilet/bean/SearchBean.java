package com.gbilet.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import com.gbilet.entity.Event;
import com.gbilet.entity.EventType;
import com.gbilet.service.DAO;

@ViewScoped
@ManagedBean(name="search", eager=true)
public class SearchBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private EntityManager entityManager;
	
	private List<Event> organizationList;
	
	private Date startDate, endDate;
	private String typeId = "1";
	private String searchString = "";
	
	private String searchType;
	
	@PostConstruct
	void initialiseSession() {
	    FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	}

	public SearchBean(){
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		UserBean userBean = (UserBean) sessionMap.get("user");
	    String ss = userBean.getSearchString();
	    Date dd = userBean.getStartDate();
		
	    searchType = "";
	    if(ss != null && !ss.equals(""))
	    	setForNameSearching(userBean, ss);
	    if(dd != null)
	    	setForDateSearching(userBean, dd);
	    
	    Calendar calendar = Calendar.getInstance();
		startDate = calendar.getTime();
		calendar.add(Calendar.DATE, 7);
		endDate = calendar.getTime();
		
		entityManager = DAO.getEntityManager();
		doSearch();
	}
	
	private void setForNameSearching(UserBean userBean, String ss) {
		searchString = ss;
		userBean.setSearchString("");
		searchType = "S";
	}
	
	private void setForDateSearching(UserBean userBean, Date dd) {
		startDate = dd;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.DATE, 1);
		endDate = calendar.getTime();
		userBean.setStartDate(null);
		searchType = "C";
	}

	public void doSearch() {
		EventType type = entityManager.find(EventType.class, Integer.parseInt(typeId));
		
		if(searchType.equals("S"))
			searchUsingName();
		else if(searchType.equals("C"))
			searchUsingDateInterval();
		else
			searchUsingAllInfo(type);
		
	}
	
	@SuppressWarnings("unchecked")
	private void searchUsingName() {
		organizationList = entityManager.createQuery("SELECT organization FROM GbOrganization organization " +
				"WHERE organization.name like :searchName ")
				.setParameter("searchName", "%"+searchString+"%").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private void searchUsingDateInterval() {
		organizationList = entityManager.createQuery("SELECT organization FROM GbOrganization organization " +
				"WHERE organization.date > :startDate " +
				"AND organization.date < :endDate ")
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private void searchUsingAllInfo(EventType type) {
		organizationList = entityManager.createQuery("SELECT organization FROM GbOrganization organization " +
								"WHERE organization.name like :searchName " +
								"AND organization.date > :startDate " +
								"AND organization.date < :endDate " +
								"AND organization.gbOrganizationType=:type")
								.setParameter("searchName", "%"+searchString+"%")
								.setParameter("startDate", startDate)
								.setParameter("endDate", endDate)
								.setParameter("type", type).getResultList();
	}

	public List<Event> getOrganizationList() {
		return organizationList;
	}
	public void setOrganizationList(List<Event> organizationList) {
		this.organizationList = organizationList;
	}

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	
}
