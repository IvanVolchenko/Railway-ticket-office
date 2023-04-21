package com.example.epam.finalProject.Railwayticketoffice.data;

import com.example.epam.finalProject.Railwayticketoffice.models.Stop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StopRepository extends CrudRepository <Stop, Long> {
    List<Stop> findByTrain (String train);

    List <Stop> findAllByStation_id(long id);
}
