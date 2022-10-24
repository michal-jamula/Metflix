package com.metflix.repositories;


import com.metflix.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

    //This interface isn't currently used anywhere in the program
}
