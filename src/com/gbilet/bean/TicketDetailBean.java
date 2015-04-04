package com.gbilet.bean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.gbilet.entity.Ticket;
import com.gbilet.service.DAO;

@ViewScoped
@ManagedBean(name="ticketDetail", eager=true)
public class TicketDetailBean {
	
	private EntityManager entityManager;
	
	private Ticket ticket;
	private boolean canRefund = true;
	
	private String id;
	
	@PostConstruct
	private void initialiseSession() {
	    FacesContext.getCurrentInstance().getExternalContext().getSession(true);	    
	}
	
	public TicketDetailBean(){
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    id = request.getParameter("id");
	    
	    entityManager = DAO.getEntityManager();
        
	    if(id!=null){
	        int idNumber = Integer.parseInt(id);
	        ticket = (Ticket)entityManager.createQuery("SELECT ticket FROM GbTicket ticket " +
	        							"WHERE ticket.id=:id")
	        							.setParameter("id", idNumber).getSingleResult();
	    }
        
	    if(ticket.getEvent().getDate().before(Calendar.getInstance().getTime()))
	    	setCanRefund(false);
	}
	
	public String getFormattedDate(){
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy EE HH:mm");
		return formatter.format(ticket.getEvent().getDate());
	}
	
	public boolean getCheckTicketCanRefund(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);
		Date tomorrow = calendar.getTime();
		
		Date ticketDate = ticket.getEvent().getDate();
		
		
		if(tomorrow.after(ticketDate))
			return false;
		else
			return true;		
	}
	
	public String refundTicket(){		
		try{
			entityManager.getTransaction().begin();
			ticket.setStatus("I");
			entityManager.persist(ticket);
			entityManager.flush();
			entityManager.getTransaction().commit();
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		
		return "listTickets?faces-redirect=true";
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public boolean isCanRefund() {
		return canRefund;
	}

	public void setCanRefund(boolean canRefund) {
		this.canRefund = canRefund;
	}


}
