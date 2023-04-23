package com.example.epam.finalProject.Railwayticketoffice.data;

import com.example.epam.finalProject.Railwayticketoffice.models.Station;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

/**
 * The repository 'StationsRepository'.
 * @author Ivan Volchenko
 */
@Repository
public interface StationsRepository extends CrudRepository<Station,Long> {
    Optional<Station> findByStreet (String street);

    ArrayList<Station> findAllByCity(String city);

    Optional<Station> findByCity(String city);

    ArrayList<Station> findAllByStreet(String street);
}
