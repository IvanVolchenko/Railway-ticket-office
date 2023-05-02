package com.example.epam.finalProject.Railwayticketoffice.models;

import jakarta.persistence.*;

/**
 * Tickets for specific train.
 * @author Ivan Volchenko
 */
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "tran")
    public String tran;
    @Column(name = "seat")
    public int seat;
    @Column(name = "uuid")
    private String uuid;
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "stop_arrival_id")
    private Stop stopArrival;
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "stop_departure_id")
    private Stop stopDeparture;

    public Ticket() {
    }

    public Ticket( String uuid, User user,Stop stopArrival, Stop stopDeparture) {
        this.uuid = uuid;
        this.user=user;
        this.stopArrival=stopArrival;
        this.stopDeparture=stopDeparture;

    }

    public Stop getStopArrival() {
        return stopArrival;
    }

    public void setStopArrival(Stop stopArrival) {
        this.stopArrival = stopArrival;
    }

    public Stop getStopDeparture() {
        return stopDeparture;
    }

    public void setStopDeparture(Stop stopDeparture) {
        this.stopDeparture = stopDeparture;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTran() {
        return tran;
    }

    public void setTran(String tran) {
        this.tran = tran;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
