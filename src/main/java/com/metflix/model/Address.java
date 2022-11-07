package com.metflix.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "Address")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;

    private Integer userId;
    private String street;
    private Integer houseNumber;
    private String postCode;


}
