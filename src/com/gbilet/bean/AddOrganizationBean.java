package com.gbilet.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.gbilet.entity.Event;
import com.gbilet.entity.EventType;
import com.gbilet.entity.User;
import com.gbilet.service.DAO;

@ViewScoped
@ManagedBean(name="addOrganization", eager=true)
public class AddOrganizationBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private EntityManager entityManager;
	
	private Event newOrganization;
	private String organizationType;
	
	private List<EventType> typeList;
	
	private boolean imageSuccess = false;
	private boolean bigimageSuccess = false;
	private boolean smallimageSuccess = false;
	
	private UploadedFile smallImageFile;
	private UploadedFile bigImageFile;
	
	@PostConstruct
	void initialiseSession() {
	    FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	}

	@SuppressWarnings("unchecked")
	public AddOrganizationBean(){
		smallImageFile = null;
		bigImageFile = null;
		
        entityManager = DAO.getEntityManager();
		
        String hql = "SELECT type FROM GbOrganizationType type";
        typeList = entityManager.createQuery(hql).getResultList();
        
        createNewOrganization();
	}

	private void createNewOrganization() {
		User user = DAO.getCurrentUser();
        
		newOrganization = new Event();
		newOrganization.setDate(Calendar.getInstance().getTime());
		newOrganization.setUser(user);
	}
	
	public void saveOrganization(){
		try{
			for (EventType type : typeList) {
				if(type.getName().equals(organizationType)){
					newOrganization.setEventType(type);
					break;
				}
			}
			
			updateOrganizationInDatabase();	
			
	        System.out.println("Başarılı");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
	}
	
	public void setSmallImage(FileUploadEvent event){
		smallImageFile = event.getFile();
	}
	
	public void setBigImage(FileUploadEvent event){
		bigImageFile = event.getFile();
	}
	
	public void saveImages(){
		if(smallImageFile!=null){
			smallimageSuccess = uploadFile(smallImageFile, newOrganization.getId() + "small.jpg");
			newOrganization.setImageSmall( newOrganization.getId() + "small.jpg");
		}
		if(bigImageFile!=null){
			bigimageSuccess = uploadFile(bigImageFile,  newOrganization.getId() + "big.jpg");
			newOrganization.setImageBig( newOrganization.getId() + "big.jpg");
		}
		imageSuccess = smallimageSuccess && bigimageSuccess;
		
		updateOrganizationInDatabase();
	}
	
	private void updateOrganizationInDatabase() {
		entityManager.getTransaction().begin();
		entityManager.persist(newOrganization);
		entityManager.flush();
		entityManager.getTransaction().commit();
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
	
	public Event getNewOrganization() {
		return newOrganization;
	}
	public void setNewOrganization(Event newOrganization) {
		this.newOrganization = newOrganization;
	}
	
	public List<EventType> getTypeList() {
		return typeList;
	}
	public void setTypeList(List<EventType> typeList) {
		this.typeList = typeList;
	}

	public String getOrganizationType() {
		return organizationType;
	}
	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}
	
	public UploadedFile getSmallImage(){
		return smallImageFile;
	}
	public UploadedFile getBigImage(){
		return bigImageFile;
	}

	public boolean isImageSuccess() {
		return imageSuccess;
	}
	public void setImageSuccess(boolean imageSuccess) {
		this.imageSuccess = imageSuccess;
	}
	
}
