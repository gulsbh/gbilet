package com.gbilet.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.gbilet.entity.Announcement;
import com.gbilet.entity.Event;
import com.gbilet.entity.EventType;
import com.gbilet.entity.Ticket;
import com.gbilet.service.DAO;

@ViewScoped
@ManagedBean(name="organizationDetail", eager=true)
public class OrganizationDetailBean {
	
	private EntityManager entityManager;
	
	private Event organization;
	private List<EventType> typeList;
	private String organizationType;
	
	private String announcement;
	
	private String id;
	
	@PostConstruct
	private void initialiseSession() {
	    FacesContext.getCurrentInstance().getExternalContext().getSession(true);	    
	}
	
	@SuppressWarnings("unchecked")
	public OrganizationDetailBean(){
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    id = request.getParameter("id");
	    
	    entityManager = DAO.getEntityManager();
        
	    if(id!=null){
	        String hql = "SELECT type FROM GbOrganizationType type";
	        typeList = entityManager.createQuery(hql).getResultList();
	        
	        
	        int idNumber = Integer.parseInt(id);
	        organization = (Event)entityManager.createQuery("SELECT organization FROM GbOrganization organization " +
	        							"WHERE organization.id=:id")
	        							.setParameter("id", idNumber).getSingleResult();
	    }
        
	}
	
	public String chooseOrganization(Event organization){
		return "organizationDetails?id=" + organization.getId();
	}
	
	public void setSmallImage(FileUploadEvent event){
		uploadFile(event.getFile(), organization.getId() + "small.jpg");
		organization.setImageSmall(organization.getId() + "small.jpg");
		
		try{
			entityManager.getTransaction().begin();
			entityManager.persist(organization);		
			entityManager.flush();
			entityManager.getTransaction().commit();
		}catch(Exception e){
			System.err.print(e.getMessage());
		}	
	}
	
	public void setBigImage(FileUploadEvent event){
		uploadFile(event.getFile(), organization.getId() + "big.jpg");
		organization.setImageBig(organization.getId() + "big.jpg");
		
		try{
			entityManager.getTransaction().begin();
			entityManager.persist(organization);		
			entityManager.flush();
			entityManager.getTransaction().commit();
		}catch(Exception e){
			System.err.print(e.getMessage());
		}
	}
	
	private boolean uploadFile(UploadedFile file, String newName){
		System.out.println("Uploading " + file.getFileName() + " as " + newName);
        try {
            File targetFolder = new File(((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("images"));
            InputStream inputStream = file.getInputstream();
            OutputStream out = new FileOutputStream(new File(targetFolder, newName));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public void createAnnouncement(){
		Announcement newAnnouncement = new Announcement();
		newAnnouncement.setEvent(organization);
		newAnnouncement.setAnnouncement(announcement);
		newAnnouncement.setDate(Calendar.getInstance().getTime());
		
		try{
			entityManager.getTransaction().begin();
			entityManager.persist(newAnnouncement);		
			entityManager.flush();
			entityManager.getTransaction().commit();
			
			Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			UserBean userBean = (UserBean) sessionMap.get("user");
			userBean.initAnnouncementList();
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Duyuru baþarýyla eklendi"));  
		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}
	}

	public void updateOrganization(){
		try{
			entityManager.getTransaction().begin();
			
			for (EventType type : typeList) {
				if(type.getName().equals(organizationType)){
					organization.setEventType(type);
					break;
				}
			}
			
			entityManager.persist(organization);		
			entityManager.flush();
			entityManager.getTransaction().commit();
		}catch(Exception e){
			System.err.print(e.getMessage());
		}
	}
	
	public boolean getCheckOrganizationCanDelete(){
		if(organization==null)
			return false;
		for (Ticket ticket : organization.getTickets()) {
			if(ticket.getUser()!=null){
				return false;
			}
		}
		return true;
	}
	
	public String deleteOrganization(){		
		try{
			entityManager.getTransaction().begin();
			
			for (Ticket ticket : organization.getTickets()) {
				entityManager.remove(ticket);
			}
			for(Announcement ann : organization.getAnnouncements()){
				entityManager.remove(ann);
			}
			
			entityManager.remove(organization);	
			entityManager.flush();
			entityManager.getTransaction().commit();
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		
		return "listOrganizations?faces-redirect=true";
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

	public List<EventType> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<EventType> typeList) {
		this.typeList = typeList;
	}

	public String getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}

	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

}
