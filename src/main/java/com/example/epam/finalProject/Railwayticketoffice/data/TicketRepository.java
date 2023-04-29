package com.example.epam.finalProject.Railwayticketoffice.data;

import com.example.epam.finalProject.Railwayticketoffice.models.Ticket;
import com.example.epam.finalProject.Railwayticketoffice.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The repository 'TicketRepository'.
 * @author Ivan Volchenko
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page <Ticket> findByUser(User user, Pageable pageable);

    List <Ticket> findAllByTran(String tran);

    Page<Ticket> findAllTicketsByTran(String tran,Pageable pageable);
}
