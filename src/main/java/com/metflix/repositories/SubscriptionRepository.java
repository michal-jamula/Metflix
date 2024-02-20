package com.metflix.repositories;

import com.metflix.model.ActiveSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SubscriptionRepository extends JpaRepository<ActiveSubscription, String> {

    Optional<ActiveSubscription> findById (String id);
}