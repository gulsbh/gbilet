package com.gbilet.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ticket database table.
 * 
 */
@Entity
@NamedQuery(name="Ticket.findAll", query="SELECT t FROM Ticket t")
public class Ticket implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="row_no")
	private int rowNo;

	@Column(name="seat_no")
	private int seatNo;

	private String status;

	//bi-directional many-to-one association to Event
	@ManyToOne
	private Event event;

	//bi-directional many-to-one association to User
	@ManyToOne
	private User user;

	public Ticket() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRowNo() {
		return this.rowNo;
	}

	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	public int getSeatNo() {
		return this.seatNo;
	}

	public void setSeatNo(int seatNo) {
		this.seatNo = seatNo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Event getEvent() {
		return this.event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}