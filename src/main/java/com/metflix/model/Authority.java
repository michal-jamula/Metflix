package com.metflix.model;


import com.metflix.model.Enums.AuthoritiesEnum;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;

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

    public Authority (int id, AuthoritiesEnum authority) {
        this.authorityId = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority1 = (Authority) o;
        return Objects.equals(authorityId, authority1.authorityId) && authority == authority1.authority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorityId, authority);
    }
}
