package com.gbilet.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the announcement database table.
 * 
 */
@Entity
@NamedQuery(name="Announcement.findAll", query="SELECT a FROM Announcement a")
public class Announcement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String announcement;

	//bi-directional many-to-one association to Event
	@ManyToOne
	private Event event;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	public Announcement() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAnnouncement() {
		return this.announcement;
	}

	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}

	public Event getEvent() {
		return this.event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}