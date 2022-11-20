package com.andev.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class UserAddress {
    private String town;
    private String street;
    private Integer houseNumber;
    private Integer apartmentNumber;
    private Integer postalCode;
}
