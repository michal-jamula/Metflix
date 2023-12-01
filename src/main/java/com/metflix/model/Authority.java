package com.metflix.model;


import com.metflix.model.Enums.AuthoritiesEnum;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="authorities")

public class Authority implements GrantedAuthority {

    @Id
    @Column(name = "authority_id")
    private Integer authorityId;

    @Enumerated(EnumType.STRING)
    private AuthoritiesEnum authority;



    public Authority() {

    }

    public Authority (AuthoritiesEnum authority) {
        this.authority = authority;
    }


    @Override
    public String getAuthority() {
        return authority.toString();
    }

    @Override
    public String toString() {
        return authority.toString();
    }

//    @Override
//    public String toString() {
//        return authority;
//    }
}
