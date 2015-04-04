package com.gbilet.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import com.gbilet.entity.EventType;
import com.gbilet.entity.Ticket;
import com.gbilet.service.DAO;

@ViewScoped
@ManagedBean(name="listTickets", eager=true)
public class ListTicketsBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private EntityManager entityManager;
	
	private Date startDate;
	private Date finishDate;
	private String type;
	private int userID;
	private List<Ticket> purchasedTicketList;
	private List<Ticket> rezervedTicketList;
	
	private List<EventType> typeList;
	
	@PostConstruct
	void initialiseSession() {
	    FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	}

	@SuppressWarnings("unchecked")
	public ListTicketsBean(){
		entityManager = DAO.getEntityManager();
		userID = DAO.getCurrentUser().getId();
		
		Calendar calendar = Calendar.getInstance();
		startDate = calendar.getTime();
		calendar.add(Calendar.DATE, 7);
		finishDate = calendar.getTime();
		
		String hql = "SELECT type FROM GbOrganizationType type";
        typeList = entityManager.createQuery(hql).getResultList();

        purchasedTicketList = entityManager.createQuery("SELECT ticket FROM GbTicket ticket WHERE ticket.gbUser.id=:id AND ticket.status=:buy")
        		.setParameter("id", userID).setParameter("buy", "B").getResultList();
        
        rezervedTicketList = entityManager.createQuery("SELECT ticket FROM GbTicket ticket WHERE ticket.gbUser.id=:id AND ticket.status=:rezerve")
        		.setParameter("id", userID).setParameter("rezerve", "R").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public void search(){
		purchasedTicketList = entityManager.createQuery("SELECT ticket FROM GbTicket ticket " +
		         "WHERE ticket.gbUser.id=:id AND ticket.status=:status " +
		         "AND ticket.gbOrganization.date > :startDate " +
		         "AND ticket.gbOrganization.date < :endDate " +
		         "AND ticket.gbOrganization.gbOrganizationType.name=:type")
		         .setParameter("id", userID)
		         .setParameter("status", "B")
		         .setParameter("startDate", startDate)
		         .setParameter("endDate", finishDate)
		         .setParameter("type", type).getResultList();
		
		rezervedTicketList = entityManager.createQuery("SELECT ticket FROM GbTicket ticket " +
		         "WHERE ticket.gbUser.id=:id AND ticket.status=:status " +
		         "AND ticket.gbOrganization.date > :startDate " +
		         "AND ticket.gbOrganization.date < :endDate " +
		         "AND ticket.gbOrganization.gbOrganizationType.name=:type")
		         .setParameter("id", userID)
		         .setParameter("status", "R")
		         .setParameter("startDate", startDate)
		         .setParameter("endDate", finishDate)
		         .setParameter("type", type).getResultList();
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<EventType> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<EventType> typeList) {
		this.typeList = typeList;
	}

	public List<Ticket> getPurchasedTicketList() {
		return purchasedTicketList;
	}

	public void setPurchasedTicketList(List<Ticket> purchasedTicketList) {
		this.purchasedTicketList = purchasedTicketList;
	}

	public List<Ticket> getRezervedTicketList() {
		return rezervedTicketList;
	}

	public void setRezervedTicketList(List<Ticket> rezervedTicketList) {
		this.rezervedTicketList = rezervedTicketList;
	}
	
}
