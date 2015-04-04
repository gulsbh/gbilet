package com.gbilet.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the event database table.
 * 
 */
@Entity
@NamedQuery(name="Event.findAll", query="SELECT e FROM Event e")
public class Event implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String address;

	@Column(name="can_reserved")
	private byte canReserved;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Lob
	private String description;

	@Column(name="image_big")
	private String imageBig;

	@Column(name="image_small")
	private String imageSmall;

	private String name;

	@Column(name="number_of_rows")
	private int numberOfRows;

	@Column(name="number_of_tickets")
	private int numberOfTickets;

	@Column(name="ticket_price")
	private float ticketPrice;

	//bi-directional many-to-one association to Announcement
	@OneToMany(mappedBy="event", fetch=FetchType.EAGER)
	private List<Announcement> announcements;

	//bi-directional many-to-one association to Comment
	@OneToMany(mappedBy="event", fetch=FetchType.EAGER)
	private List<Comment> comments;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="organizator_id")
	private User user;

	//uni-directional many-to-one association to EventType
	@ManyToOne
	@JoinColumn(name="type")
	private EventType eventType;

	//bi-directional many-to-one association to Rate
	@OneToMany(mappedBy="event", fetch=FetchType.EAGER)
	private List<Rate> rates;

	//bi-directional many-to-one association to Ticket
	@OneToMany(mappedBy="event", fetch=FetchType.EAGER)
	private List<Ticket> tickets;

	public Event() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public byte getCanReserved() {
		return this.canReserved;
	}

	public void setCanReserved(byte canReserved) {
		this.canReserved = canReserved;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageBig() {
		return this.imageBig;
	}

	public void setImageBig(String imageBig) {
		this.imageBig = imageBig;
	}

	public String getImageSmall() {
		return this.imageSmall;
	}

	public void setImageSmall(String imageSmall) {
		this.imageSmall = imageSmall;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfRows() {
		return this.numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public int getNumberOfTickets() {
		return this.numberOfTickets;
	}

	public void setNumberOfTickets(int numberOfTickets) {
		this.numberOfTickets = numberOfTickets;
	}

	public float getTicketPrice() {
		return this.ticketPrice;
	}

	public void setTicketPrice(float ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public List<Announcement> getAnnouncements() {
		return this.announcements;
	}

	public void setAnnouncements(List<Announcement> announcements) {
		this.announcements = announcements;
	}

	public Announcement addAnnouncement(Announcement announcement) {
		getAnnouncements().add(announcement);
		announcement.setEvent(this);

		return announcement;
	}

	public Announcement removeAnnouncement(Announcement announcement) {
		getAnnouncements().remove(announcement);
		announcement.setEvent(null);

		return announcement;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Comment addComment(Comment comment) {
		getComments().add(comment);
		comment.setEvent(this);

		return comment;
	}

	public Comment removeComment(Comment comment) {
		getComments().remove(comment);
		comment.setEvent(null);

		return comment;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public EventType getEventType() {
		return this.eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public List<Rate> getRates() {
		return this.rates;
	}

	public void setRates(List<Rate> rates) {
		this.rates = rates;
	}

	public Rate addRate(Rate rate) {
		getRates().add(rate);
		rate.setEvent(this);

		return rate;
	}

	public Rate removeRate(Rate rate) {
		getRates().remove(rate);
		rate.setEvent(null);

		return rate;
	}

	public List<Ticket> getTickets() {
		return this.tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public Ticket addTicket(Ticket ticket) {
		getTickets().add(ticket);
		ticket.setEvent(this);

		return ticket;
	}

	public Ticket removeTicket(Ticket ticket) {
		getTickets().remove(ticket);
		ticket.setEvent(null);

		return ticket;
	}

}