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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "tran")
    public String tran;
    @Column(name = "seat")
    public int seat;
    @Column(name = "uuid")
    private String uuid;
//    @Column(name = "name")
//    private String name;
//    @Column(name = "document")
//    public String document;
    @Column(name = "departure")
    private String departure;
    @Column(name = "arrival")
    public String arrival;
    @Column(name = "depTime")
    public String depTime;
    @Column(name = "arTime")
    public String arTime;
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;
    public Ticket() {
    }


    public Ticket(String tran, int seat, String uuid, User user,
                  String departure, String arrival, String depTime,String arTime) {
//        public Ticket(String tran, int seat, String uuid, String name, String document,
//                String departure, String arrival, String date) {
        this.tran = tran;
        this.seat = seat;
        this.uuid = uuid;
        this.user=user;
//        this.name = name;
//        this.document = document;
        this.departure = departure;
        this.arrival = arrival;
        this.depTime = depTime;
        this.arTime=arTime;
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
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDocument() {
//        return document;
//    }
//
//    public void setDocument(String document) {
//        this.document = document;
//    }
//
    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }

    public String getArTime() {
        return arTime;
    }

    public void setArTime(String arTime) {
        this.arTime = arTime;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", tran='" + tran + '\'' +
                ", seat=" + seat +
                ", uuid='" + uuid + '\'' +
                ", departure='" + departure + '\'' +
                ", arrival='" + arrival + '\'' +
                ", depTime='" + depTime + '\'' +
                ", arTime='" + arTime + '\'' +
                ", user=" + user +
                '}';
    }
}
