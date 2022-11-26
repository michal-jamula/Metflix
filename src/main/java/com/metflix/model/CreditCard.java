package com.metflix.model;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="c_card")
public class CreditCard {

    //This table isn't currently used anywhere in the program

    @NonNull
    private Integer userId;

    @Id
    @Column(name="c_number", length = 16)
    @NonNull
    private String cardNumber;

    @Column(name="exp_date", length = 4)
    @NonNull
    private String expDate;

    @NonNull
    @Column(length = 3)
    private String cvc;
}
