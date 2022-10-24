package com.metflix.model;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    //This table isn't currently used anywhere in the program

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="address_id")
    private Integer addressId;

    @Column(name="user_id")
    private Integer userId;

    @Column(name="house_number")
    private String houseNumber;

    private String street;
    private String postCode;

}
