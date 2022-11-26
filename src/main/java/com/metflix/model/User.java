package com.metflix.model;

import com.metflix.model.modelEnum.UserStatusEnum;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")

public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String surname;
    private String email;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate dob;
    @Column(name="phone_nr")
    private String phoneNr;
    @Column(name="reg_date")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate regDate;
    private UserStatusEnum status;
    @Column (name="pwd")
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





    public User(String name, String surname, String email, LocalDate dob, String phoneNr, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.dob = dob;
        this.phoneNr = phoneNr;
        this.password = password;
        this.status = UserStatusEnum.unsubscribed;
        this.regDate = LocalDate.now();
    }
}
