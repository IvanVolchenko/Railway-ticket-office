package com.example.epam.finalProject.Railwayticketoffice.data;

import com.example.epam.finalProject.Railwayticketoffice.models.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The repository 'StopRepository'.
 * @author Ivan Volchenko
 */
@Repository
public interface StopRepository extends JpaRepository<Stop, Long> {
    List<Stop> findByTrain (String train);

    List <Stop> findAllByStation_id(long id);

    List<Stop> findAllByTrain(String train);
}
