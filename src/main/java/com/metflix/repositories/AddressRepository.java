package com.metflix.repositories;

import com.metflix.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    //This interface isn't currently used anywhere in the program

}
