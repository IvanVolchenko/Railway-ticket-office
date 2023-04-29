package com.example.epam.finalProject.Railwayticketoffice.models;


/**
 * Route between stops.
 * @author Ivan Volchenko
 */
public class Route {

    private String  number;
    private String arrival;
    private String stationFirst;
    private String time;
    private String departure;
    private String stationSecond;
    private String street;
    private double value;
    private int seats;
    private int km;


    private long firstId;
    private long secondId;

    public Route(String number, String arrival, String stationFirst,
                 String time, String departure, String stationSecond,
                 double value, int seats, long firstId, long secondId) {
        this.number = number;
        this.arrival = arrival;
        this.stationFirst = stationFirst;
        this.time = time;
        this.departure = departure;
        this.stationSecond = stationSecond;
        this.value = value;
        this.seats = seats;
        this.firstId = firstId;
        this.secondId = secondId;
    }

    public Route(String number, String arrival, String stationFirst,
                  String departure, int km, long firstId, long secondId) {
        this.number = number;
        this.arrival = arrival;
        this.stationFirst = stationFirst;
        this.departure = departure;
        this.km = km;
        this.firstId = firstId;
        this.secondId = secondId;
    }

    public Route() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getStationFirst() {
        return stationFirst;
    }

    public void setStationFirst(String stationFirst) {
        this.stationFirst = stationFirst;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getStationSecond() {
        return stationSecond;
    }

    public void setStationSecond(String stationSecond) {
        this.stationSecond = stationSecond;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public long getFirstId() {
        return firstId;
    }

    public void setFirstId(long firstId) {
        this.firstId = firstId;
    }

    public long getSecondId() {
        return secondId;
    }

    public void setSecondId(long secondId) {
        this.secondId = secondId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }

    @Override
    public String toString() {
        return "Route{" +
                "number='" + number + '\'' +
                ", arrival=" + arrival +
                ", stationFirst='" + stationFirst + '\'' +
                ", time='" + time + '\'' +
                ", departure=" + departure +
                ", stationSecond='" + stationSecond + '\'' +
                ", value=" + value +
                ", seats=" + seats +
                ", firstId=" + firstId +
                ", secondId=" + secondId +
                '}';
    }
}
