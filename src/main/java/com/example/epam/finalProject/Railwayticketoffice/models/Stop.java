package com.example.epam.finalProject.Railwayticketoffice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Stops of routes.
 * @author Ivan Volchenko
 */
@Entity
@Table(name = "stops")
public class Stop {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "train")
    private String train;
    @Column(name = "arrival")
    @NotNull(message = "Arrival in should not be empty")
    @Future
    private LocalDateTime arrival;
    @Column(name = "departure")
    @NotNull(message = "Departure in should not be empty")
    @Future
    private LocalDateTime departure;
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "station_id")
    private Station station;
    @Column(name = "price")
    @Min(value = 0,message = "Price should be greater than 0")
    private double price = 0.05;
    @Column(name = "km")
    @Min(value = 0,message = "Km should be greater than 0")
    private int km;
    @Column(name = "seats")
    private int seats = 180;
    @OneToMany(mappedBy = "stopArrival",
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Ticket> ticketsArrival;
    @OneToMany(mappedBy = "stopDeparture",
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Ticket> ticketsDeparture;


    public Stop() {
    }

    public Stop(String train, LocalDateTime arrival, LocalDateTime departure, int km) {
        this.train = train;
        this.arrival = arrival;
        this.departure = departure;
        this.km = km;
    }

    public List<Ticket> getTicketsArrival() {
        return ticketsArrival;
    }

    public void setTicketsArrival(List<Ticket> ticketsArrival) {
        if (ticketsArrival!=null){
            ticketsArrival.forEach(t->t.setStopArrival(this));
        }
        this.ticketsArrival = ticketsArrival;
    }

    public List<Ticket> getTicketsDeparture() {
        return ticketsDeparture;
    }

    public void setTicketsDeparture(List<Ticket> ticketsDeparture) {
        if (ticketsDeparture!=null){
            ticketsDeparture.forEach(t->t.setStopDeparture(this));
        }
        this.ticketsDeparture = ticketsDeparture;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTrain() {
        return train;
    }

    public void setTrain(String train) {
        this.train = train;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = Math.abs(price);
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public LocalDateTime getArrival() {
        return arrival;
    }

    public void setArrival(LocalDateTime arrival) {
        this.arrival = arrival;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDateTime departure) {
        this.departure = departure;
    }

    @Override
    public String toString() {
        return "Stop{" +
                "id=" + id +
                ", train='" + train + '\'' +
                ", arrival=" + arrival +
                ", departure=" + departure +
                ", station=" + station +
                ", price=" + price +
                ", km=" + km +
                ", seats=" + seats +
                '}';
    }
}
