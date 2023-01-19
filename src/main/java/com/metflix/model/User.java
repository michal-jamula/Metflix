package com.metflix.model;

import com.metflix.model.modelEnum.UserStatusEnum;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String surname;
    private String email;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String phoneNumber;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate registrationDate;
    private UserStatusEnum status;
    private String password;

/*
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column(name="c_card")
    @Nullable
    private Set<CreditCard> creditCards;


    @OneToMany (fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(joinColumns = @JoinColumn(name = "id"), inverseJoinColumns = @JoinColumn(name = "userId"))
    @Nullable
    private Collection<Address> Address;
*/


//    public User(String name, String surname, String email, LocalDate dateOfBirth, String phoneNumber, String password) {
//        this.name = name;
//        this.surname = surname;
//        this.email = email;
//        this.dateOfBirth = dateOfBirth;
//        this.phoneNumber = phoneNumber;
//        this.password = password;
//        this.status = UserStatusEnum.UNSUBSCRIBED;
//        this.registrationDate = LocalDate.now();
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
