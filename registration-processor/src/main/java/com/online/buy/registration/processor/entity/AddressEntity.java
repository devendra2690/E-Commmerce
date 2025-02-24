package com.online.buy.registration.processor.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    private String street1;
    private String street2;
    private String landmark;
    private String city;
    private String postalCode;
    private String state;
    private String country;

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    @JsonBackReference
    private ConsumerEntity consumer;

}
