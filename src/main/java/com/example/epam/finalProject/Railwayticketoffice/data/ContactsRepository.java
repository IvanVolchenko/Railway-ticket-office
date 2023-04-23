package com.example.epam.finalProject.Railwayticketoffice.data;

import com.example.epam.finalProject.Railwayticketoffice.models.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository 'ContactsRepository'.
 * @author Ivan Volchenko
 */
@Repository
public interface ContactsRepository extends CrudRepository<Contact,Long> {
}
