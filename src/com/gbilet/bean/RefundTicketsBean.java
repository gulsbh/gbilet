package com.gbilet.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import com.gbilet.entity.Ticket;
import com.gbilet.service.DAO;

@ViewScoped
@ManagedBean(name="refundTickets", eager=true)
public class RefundTicketsBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private EntityManager entityManager;
	
	private List<Ticket> refundTicketList;
	
	@PostConstruct
	void initialiseSession() {
	    FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	}

	
	public RefundTicketsBean(){
		entityManager = DAO.getEntityManager();
		initRefundTickets();
	}
	
	@SuppressWarnings("unchecked")
	private void initRefundTickets() {
		refundTicketList = entityManager.createQuery("SELECT ticket FROM GbTicket ticket " +
								"WHERE ticket.status=:status")
								.setParameter("status", "I").getResultList();
	}
	
	public void validateRefundTicket(Integer ticketId){
		try{
			entityManager.getTransaction().begin();
			
			for (Ticket ticket : refundTicketList) {
				if(ticket.getId() == ticketId){
					entityManager.remove(ticket);
					break;
				}
			}
					
			entityManager.flush();
			entityManager.getTransaction().commit();
			initRefundTickets();
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO , "Baþarýlý", "Onaylama iþlemi baþarýlý."));
		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR , "HATA", 
							"Onaylama iþlemi baþarýsýz."));
			
			entityManager.getTransaction().rollback();
		}
	}


	public List<Ticket> getRefundTicketList() {
		return refundTicketList;
	}


	public void setRefundTicketList(List<Ticket> refundTicketList) {
		this.refundTicketList = refundTicketList;
	}

}
