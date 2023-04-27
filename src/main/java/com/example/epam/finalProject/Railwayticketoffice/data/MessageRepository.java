package com.example.epam.finalProject.Railwayticketoffice.data;

import com.example.epam.finalProject.Railwayticketoffice.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository 'MessageRepository'.
 * @author Ivan Volchenko
 */
@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
}
