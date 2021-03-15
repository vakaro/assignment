package com.kpap.dialecticaAssignment.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kpap.dialecticaAssignment.model.User;

/**
 * @author k.papageorgiou
 *
 */
public interface UserRepository extends CrudRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.email = :email ")
	User findByEmail(@Param("email") String name);
}
