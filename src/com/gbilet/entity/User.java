package com.gbilet.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String email;

	private String name;

	private String password;

	private String role;

	private String surname;

	private String telephone;

	private String username;

	//bi-directional many-to-one association to Event
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	private List<Event> events;

	//bi-directional many-to-one association to Favourite
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	private List<Favourite> favourites;

	//bi-directional many-to-one association to Rate
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	private List<Rate> rates;

	//bi-directional many-to-one association to Ticket
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	private List<Ticket> tickets;

	public User() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Event> getEvents() {
		return this.events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public Event addEvent(Event event) {
		getEvents().add(event);
		event.setUser(this);

		return event;
	}

	public Event removeEvent(Event event) {
		getEvents().remove(event);
		event.setUser(null);

		return event;
	}

	public List<Favourite> getFavourites() {
		return this.favourites;
	}

	public void setFavourites(List<Favourite> favourites) {
		this.favourites = favourites;
	}

	public Favourite addFavourite(Favourite favourite) {
		getFavourites().add(favourite);
		favourite.setUser(this);

		return favourite;
	}

	public Favourite removeFavourite(Favourite favourite) {
		getFavourites().remove(favourite);
		favourite.setUser(null);

		return favourite;
	}

	public List<Rate> getRates() {
		return this.rates;
	}

	public void setRates(List<Rate> rates) {
		this.rates = rates;
	}

	public Rate addRate(Rate rate) {
		getRates().add(rate);
		rate.setUser(this);

		return rate;
	}

	public Rate removeRate(Rate rate) {
		getRates().remove(rate);
		rate.setUser(null);

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
		ticket.setUser(this);

		return ticket;
	}

	public Ticket removeTicket(Ticket ticket) {
		getTickets().remove(ticket);
		ticket.setUser(null);

		return ticket;
	}

}