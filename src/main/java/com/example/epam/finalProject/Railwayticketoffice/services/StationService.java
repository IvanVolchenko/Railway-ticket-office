package com.example.epam.finalProject.Railwayticketoffice.services;

import com.example.epam.finalProject.Railwayticketoffice.controllers.MainController;
import com.example.epam.finalProject.Railwayticketoffice.data.StationsRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class StationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StationService.class);
    private final StationsRepository stationsRepository;

    public StationService(StationsRepository stationsRepository) {
        this.stationsRepository = stationsRepository;
    }

    public boolean addNewStation (Station station){
        LOGGER.info("StationService: method 'addNewStation'");
        ArrayList<Station> stationCheck = stationsRepository.findAllByStreet(station.getStreet());
        ArrayList<Station> stationCheck2 = stationsRepository.findAllByCity(station.getCity());
        for (Station station1:stationCheck){
            for (Station station2:stationCheck2){
                if (station1.getStreet().equals(station2.getStreet())){
                    return false;
                }
            }
        }
        stationsRepository.save(station);
        return true;
    }
}
