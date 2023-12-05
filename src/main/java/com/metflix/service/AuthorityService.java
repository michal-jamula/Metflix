package com.metflix.service;

import com.metflix.model.Authority;
import com.metflix.repositories.AuthorityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }


    public Page<Authority> findPaginated(final int pageNumber, final int pageSize,
                                         final String sortField, final String sortDirection) {


        Sort sort;
        if( sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())) {
            sort = Sort.by(sortField).ascending();
        } else {
            sort = Sort.by(sortField).descending();
        }


        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return authorityRepository.findAll(pageable);
    }
}
