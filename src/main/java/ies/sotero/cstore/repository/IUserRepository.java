package ies.sotero.cstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ies.sotero.cstore.model.CustomUser;

@Repository
public interface IUserRepository extends JpaRepository<CustomUser, Integer> {
	Optional<CustomUser> findByEmail(String email);

}
