package com.example.epam.finalProject.Railwayticketoffice.data;

import com.example.epam.finalProject.Railwayticketoffice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The repository 'UserRepository'.
 * @author Ivan Volchenko
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> { //CrudRepository<User,Long> {
    Optional <User> findByUsername (String username);

    Optional<User> findByEmailAddress(String emailAddress);

    Optional<User> findByDocumentNumber(String documentNumber);
}
