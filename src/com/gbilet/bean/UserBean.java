package com.gbilet.bean;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.primefaces.context.RequestContext;

import com.gbilet.entity.Announcement;
import com.gbilet.entity.User;

@ManagedBean(name="user")
@SessionScoped
public class UserBean {
	
	private EntityManager entityManager;
	
	private String userName;
	private String auth;
	private String cryptedPass;
	private Boolean isLogged = false;
	private User user;
	private User newUser = new User();
	private String newPassword = "";
	private boolean isNewUserCreationSuccess = false;
	private String editOldPassword;
	private String editNewPassword;
	private String editNewPasswordRepeat;
	private String searchString;
	private Date startDate;
	private List<Announcement> announcementList;
	
	public UserBean(){
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GBilet");
        entityManager = entityManagerFactory.createEntityManager();
        
        initAnnouncementList();
	}
	
	@SuppressWarnings("unchecked")
	public void initAnnouncementList(){
		announcementList = entityManager.createQuery("SELECT ann FROM GbAnnouncement ann ORDER BY ann.date").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public void login(){
		String hql = "SELECT user FROM GbUser user WHERE user.username=:username " +
				"AND user.password=:password";
		
		
        List<User> userResult = entityManager.createQuery(hql)
        		.setParameter("username", userName)
        		.setParameter("password", cryptedPass).getResultList();
        
		if (userResult.size() > 0) {
			if(userResult.get(0).getRole().equals("W")){
				clearUserInfo();
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR , "HATA", "Organizatör, sistem yöneticisi tarafından henüz onaylanmamış."));
			}
			else{
				setUserInfo(userResult.get(0));
				RequestContext.getCurrentInstance().execute("uyeGirisiDlg.hide()");
			}
		} else {
			clearUserInfo();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR , "HATA", "Kullanıcı adı ya da şifre hatalı"));
		}
        
	}

	private void setUserInfo(User userInfo) {
		isLogged = true;
		user = userInfo;
		auth = user.getRole();
	}

	private void clearUserInfo() {
		isLogged = false;
		user = null;
		auth = "";
	}
	
	public String logout(){
		clearUserInfo();
		return "index?faces-redirect=true";
	}
	
	public void createNewUser(){
		entityManager.getTransaction().begin();
		try{
			if(!newUser.getPassword().equals(newPassword)){
				throw new Exception("Şifre doğrulama başarısız.");
			}
			
			entityManager.persist(newUser);
			entityManager.flush();
			
			newUser = new User();
			newPassword = "";
			
			entityManager.getTransaction().commit();
			RequestContext.getCurrentInstance().execute("uyeOlDlg.hide()");
		}catch(PersistenceException e){
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR , "HATA", "Girilen kullanıcı adı ile kayıtlı başka kullanıcı vardır."));
			entityManager.getTransaction().rollback();
		}
		catch(Exception e){
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR , "HATA", e.getMessage()));
			entityManager.getTransaction().rollback();
		}		
	}
	
	public void editUser(){
		newUser = new User();
		
		newUser.setName(user.getName());
		newUser.setSurname(user.getSurname());
		newUser.setEmail(user.getEmail());
		newUser.setTelephone(user.getTelephone());
		
		RequestContext.getCurrentInstance().execute("uyelikBilgilerimDlg.show();");
	}
	
	public void confirmEdit(){
		user.setName(newUser.getName());
		user.setSurname(newUser.getSurname());
		user.setEmail(newUser.getEmail());
		user.setTelephone(newUser.getTelephone());
		
		entityManager.getTransaction().begin();
		entityManager.merge(user);
		entityManager.flush();
		entityManager.getTransaction().commit();
		
		RequestContext.getCurrentInstance().execute("uyelikBilgilerimDlg.hide();");
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO , "BAŞARILI", "Üyelik bilgileri başarılı bir şekilde güncellendi."));
	}
	
	public void confirmPasswordChange(){
		try{
			if(!user.getPassword().equals(editOldPassword))
				throw new Exception("Eski şifre hatalı");
			else if(!editNewPassword.equals(editNewPasswordRepeat))
				throw new Exception("Girilen iki yeni şifre tutmuyor");
			else{
				entityManager.getTransaction().begin();
				user.setPassword(editNewPassword);
				entityManager.merge(user);
				entityManager.flush();
				entityManager.getTransaction().commit();
				RequestContext.getCurrentInstance().execute("sifremiDegistirDlg.hide();");
				
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO , "Başarılı", "Şifre başarılı bir şekilde değiştirildi."));
			}
		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR , "HATA", e.getMessage()));
		}
	}
	
	public String searchRedirect(){
		searchString = "";
		return "search?faces-redirect=true";
	}

	public void dateChange(AjaxBehaviorEvent event) {
	    System.out.println("File Date: " + startDate);
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getCryptedPass() {
		return cryptedPass;
	}
	public void setCryptedPass(String cryptedPass) {
		this.cryptedPass = cryptedPass;
	}
	
	public Boolean getIsLogged() {
		return isLogged;
	}
	public void setIsLogged(Boolean isLogged) {
		this.isLogged = isLogged;
	}
	
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public User getNewUser() {
		return newUser;
	}
	public void setNewUser(User newUser) {
		this.newUser = newUser;
	}

	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public boolean isNewUserCreationSuccess() {
		return isNewUserCreationSuccess;
	}
	public void setNewUserCreationSuccess(boolean isNewUserCreationSuccess) {
		this.isNewUserCreationSuccess = isNewUserCreationSuccess;
	}

	public String getEditOldPassword() {
		return editOldPassword;
	}
	public void setEditOldPassword(String editOldPassword) {
		this.editOldPassword = editOldPassword;
	}

	public String getEditNewPassword() {
		return editNewPassword;
	}
	public void setEditNewPassword(String editNewPassword) {
		this.editNewPassword = editNewPassword;
	}

	public String getEditNewPasswordRepeat() {
		return editNewPasswordRepeat;
	}
	public void setEditNewPasswordRepeat(String editNewPasswordRepeat) {
		this.editNewPasswordRepeat = editNewPasswordRepeat;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public List<Announcement> getAnnouncementList() {
		return announcementList;
	}
	public void setAnnouncementList(List<Announcement> announcementList) {
		this.announcementList = announcementList;
	}

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
}
