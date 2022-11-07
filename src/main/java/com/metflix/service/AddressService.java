package com.metflix.service;

import com.metflix.model.Address;

import com.metflix.repositories.AddressRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Page<Address> findPaginated(final int pageNumber, final int pageSize,
                                       final String sortField, final String sortDirection) {


        Sort sort;
        if( sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())) {
            sort = Sort.by(sortField).ascending();
        } else {
            sort = Sort.by(sortField).descending();
        }


        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return addressRepository.findAll(pageable);
    }
}
