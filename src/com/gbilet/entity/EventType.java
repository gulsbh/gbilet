package com.gbilet.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the event_type database table.
 * 
 */
@Entity
@Table(name="event_type")
@NamedQuery(name="EventType.findAll", query="SELECT e FROM EventType e")
public class EventType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Lob
	private String description;

	private String name;

	public EventType() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}