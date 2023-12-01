package com.metflix.repositories;

import com.metflix.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    @Modifying
    @Query(value = "INSERT INTO authorities (authority_id, user_id, authority) VALUES (4, :userId, :authority)", nativeQuery = true)
    void addAuthority(@Param("userId") Integer userId, @Param("authority") String authority);


    @Query(value = "SELECT * FROM authorities WHERE user_id = :userId", nativeQuery = true)
    List<Authority> findAuthoritiesByUserId(@Param("userId") Integer userId);

    //    @Query(value = "INSERT INTO authorities (authority_id, user_id, authority) VALUES (:authorityId, :userId, :authority)", nativeQuery = true)
}