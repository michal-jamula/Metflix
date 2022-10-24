package com.metflix.model;


import lombok.*;

import javax.persistence.*;

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
    @Column(name="c_number")
    @NonNull
    private String cardNumber;

    @Column(name="exp_date")
    @NonNull
    private String expDate;

    @NonNull
    private String cvc;
}
