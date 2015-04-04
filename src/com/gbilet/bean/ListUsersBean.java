package com.gbilet.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import com.gbilet.entity.User;
import com.gbilet.service.DAO;

@ViewScoped
@ManagedBean(name="listUsers", eager=true)
public class ListUsersBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private EntityManager entityManager;
	
	private List<User> userList;
	private int choosenUserId;
	private String choosenRole;
	
	@PostConstruct
	void initialiseSession() {
	    FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	}

	
	public ListUsersBean(){
		entityManager = DAO.getEntityManager();
		initUserList();
	}
	
	@SuppressWarnings("unchecked")
	private void initUserList() {
		userList = entityManager.createQuery("SELECT user FROM GbUser user " +
								"WHERE user.role=:organizator OR user.role=:customer OR user.role=:wait")
								.setParameter("organizator", "O").setParameter("customer", "C")
								.setParameter("wait", "W").getResultList();
	}

	public void chooseUser(Integer userId){
		choosenUserId = userId;
	}
	
	
	public void validateOrganizator(Integer userId){
		try{
			entityManager.getTransaction().begin();
			
			for (User user : userList) {
				if(user.getId() == userId){
					user.setRole("O");
					entityManager.persist(user);
					break;
				}
			}
					
			entityManager.flush();
			entityManager.getTransaction().commit();
			initUserList();
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO , "Ba�ar�l�", "Onaylama i�lemi ba�ar�l�."));
		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR , "HATA", 
							"Onaylama i�lemi ba�ar�s�z."));
			
			entityManager.getTransaction().rollback();
		}
	}
	
	public void deleteUser(){
		try{
			entityManager.getTransaction().begin();
			
			for (User user : userList) {
				if(user.getId() == choosenUserId){
					choosenRole = user.getRole();
					entityManager.remove(user);
					break;
				}
			}
					
			entityManager.flush();
			entityManager.getTransaction().commit();
			initUserList();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO , "Ba�ar�l�", "Kullan�c� ba�ar�yla silindi."));
		}catch(Exception e){
			createErrorMessage();
			entityManager.getTransaction().rollback();
		}		
	}
	
	private void createErrorMessage() {
		if(choosenRole.equals("O")){
			FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR , "HATA", 
						"Se�ili organizat�r�n kay�tl� etkinliklieri bulundu�u i�in silme i�lemi ba�ar�s�zd�r."));
		}
		else if(choosenRole.equals("C")){
			FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR , "HATA",
						"Se�ili kullan�c�n�n kay�tl� biletleri bulundu�u i�in silme i�lemi ba�ar�s�zd�r."));
		}
	}
	
	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
}
