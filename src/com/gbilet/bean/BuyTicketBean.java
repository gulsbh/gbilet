package com.gbilet.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;


import com.gbilet.entity.Event;
import com.gbilet.entity.Ticket;
import com.gbilet.entity.User;
import com.gbilet.service.DAO;

@ViewScoped
@ManagedBean(name="buyTicket", eager=true)
public class BuyTicketBean {
	
	private EntityManager entityManager;
	
	private String organizationId;
	
	private Event organization;
	private HashMap<String, String> chosenTickets;
	private List<Ticket> buyingTickets;
	private Float totalPrice = new Float(0);
	
	private List<List<Ticket>> ticketList;
	
	private int operationStep = 1;
	
	@PostConstruct
	private void initialiseSession() {
	    FacesContext.getCurrentInstance().getExternalContext().getSession(true);	    
	}
	
	public BuyTicketBean(){
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    organizationId = request.getParameter("id");
	    
	    entityManager = DAO.getEntityManager();
	    
	    if(organizationId!=null){       
	        int idNumber = Integer.parseInt(organizationId);
	        organization = (Event)entityManager.createQuery("SELECT organization FROM GbOrganization organization " +
	        							"WHERE organization.id=:id")
	        							.setParameter("id", idNumber).getSingleResult();
	        
	        initTickets();
	    }
	}

	private void initTickets() {
		ticketList = new ArrayList<List<Ticket>>();
        chosenTickets = new HashMap<String, String>();
        
		for(int i=0; i<organization.getNumberOfRows(); i++){
			ticketList.add(new ArrayList<Ticket>());
			for(int j=0; j<organization.getNumberOfTickets()/organization.getNumberOfRows(); j++){
				Ticket ticket;
				if((ticket = isTicketExist(i+1, j+1)) != null)
					ticketList.get(i).add(ticket);
				else{
					ticket = createEmptyTickets(i+1, j+1);
					ticketList.get(i).add(ticket);
				}
				chosenTickets.put(ticket.getRowNo() + "-" + ticket.getSeatNo(), "false");
			}
		}
	}

	private Ticket createEmptyTickets(int row, int seat) {
		Ticket ticket;
		ticket = new Ticket();
		ticket.setRowNo(row);
		ticket.setSeatNo(seat);
		ticket.setStatus("W");
		return ticket;
	}
	
	private Ticket isTicketExist(int row, int seat) {
		for (Ticket ticket : organization.getTickets()) {
			if(ticket.getRowNo()==row && ticket.getSeatNo()==seat)
				return ticket;
		}
		return null;
	}

	public void reserveSelectedTickets(){
		entityManager.getTransaction().begin();
		
		User user = DAO.getCurrentUser();
		
		try{
			for (String ticketKey : chosenTickets.keySet()) {
				if(chosenTickets.get(ticketKey).equals("true")){
					Ticket ticket = createTicket(user, ticketKey, "R");
					entityManager.persist(ticket);
				}
			}
			
			entityManager.flush();
			entityManager.getTransaction().commit();

			operationStep=1;
			RequestContext.getCurrentInstance().execute("rezerveTamamDlg.show();");
			initTickets();
		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "HATA", e.getMessage()));
			entityManager.getTransaction().rollback();
		}
	}

	public void buySelectedTickets(){
		User user = DAO.getCurrentUser();
		
		buyingTickets = new ArrayList<Ticket>();
		
		try{
			for (String ticketKey : chosenTickets.keySet()) {
				if(chosenTickets.get(ticketKey).equals("true")){
					Ticket ticket = createTicket(user, ticketKey, "B");
					buyingTickets.add(ticket);
				}
			}
			
			totalPrice = organization.getTicketPrice() * buyingTickets.size();
			operationStep=2;
		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "HATA", e.getMessage()));
			entityManager.getTransaction().rollback();
		}
	}
	
	private Ticket createTicket(User user, String ticketKey, String status) {
		String[] rowSeat = ticketKey.split("-");
		Ticket ticket = new Ticket();
		ticket.setRowNo(Integer.parseInt(rowSeat[0]));
		ticket.setSeatNo(Integer.parseInt(rowSeat[1]));
		ticket.setUser(user);
		ticket.setStatus(status);
		ticket.setEvent(organization);
		return ticket;
	}
	
	public void confirmSelectedTickets(){
		entityManager.getTransaction().begin();
		
		try{
			for (Ticket ticket : buyingTickets) 
				entityManager.persist(ticket);
			entityManager.flush();
			entityManager.getTransaction().commit();
			RequestContext.getCurrentInstance().execute("satinAlmaTamamDlg.show();");
			initTickets();
		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "HATA", e.getMessage()));
			entityManager.getTransaction().rollback();
		}
		operationStep=1;
	}
	
	public String createKey(String row, String seat){
		return row + "-" + seat;
	}
	
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	
	public Event getOrganization(){
		return organization;
	}
	public void setOrganization(Event organization){
		this.organization = organization;
	}
	
	public HashMap<String, String> getChosenTickets() {
		return chosenTickets;
	}
	public void setChosenTickets(HashMap<String, String> chosenTickets) {
		this.chosenTickets = chosenTickets;
	}

	public List<List<Ticket>> getTicketList() {
		return ticketList;
	}
	public void setTicketList(List<List<Ticket>> ticketList) {
		this.ticketList = ticketList;
	}

	public int getOperationStep() {
		return operationStep;
	}
	public void setOperationStep(int operationStep) {
		this.operationStep = operationStep;
	}

	public List<Ticket> getBuyingTickets() {
		return buyingTickets;
	}
	public void setBuyingTickets(List<Ticket> buyingTickets) {
		this.buyingTickets = buyingTickets;
	}

	public Float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Float totalPrice) {
		this.totalPrice = totalPrice;
	}

}
