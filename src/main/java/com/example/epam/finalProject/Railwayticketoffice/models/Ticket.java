package com.example.epam.finalProject.Railwayticketoffice.models;

import jakarta.persistence.*;

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
    @Column(name = "name")
    private String name;
    @Column(name = "document")
    public String document;
    @Column(name = "departure")
    private String departure;
    @Column(name = "arrival")
    public String arrival;
    @Column(name = "date")
    public String date;
    public Ticket() {
    }


    public Ticket(String tran, int seat, String uuid, String name, String document,
                  String departure, String arrival, String date) {
        this.tran = tran;
        this.seat = seat;
        this.uuid = uuid;
        this.name = name;
        this.document = document;
        this.departure = departure;
        this.arrival = arrival;
        this.date = date;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
