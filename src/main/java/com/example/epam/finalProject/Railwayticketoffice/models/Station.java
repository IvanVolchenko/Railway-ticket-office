package com.example.epam.finalProject.Railwayticketoffice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

/**
 * Stations of country.
 * @author Ivan Volchenko
 */
@Entity
@Table(name = "stations")
public class Station {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "city")
    @NotBlank(message = "City should not be empty")
    @Size(min = 2, max = 15, message = "City should be between 2 and 15 characters" )
    private String city;
    @Column(name = "street")
    @NotBlank(message = "Street should not be empty")
    private String street;

    @OneToMany(mappedBy = "station",
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Stop> stop;

    public Station() {
    }

    public Station(String city, String address) {
        this.city = city;
        this.street = address;
    }

    public List<Stop> getStop() {
        return stop;
    }



    public void setStop(List<Stop> stop) {
        if (stop!=null){
            stop.forEach(s->s.setStation(this));
        }
        this.stop = stop;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", address='" + street + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return id == station.id && Objects.equals(city, station.city) && Objects.equals(street, station.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, city, street);
    }
}
