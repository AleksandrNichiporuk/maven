package com.andev.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "user")
@EqualsAndHashCode(of = "email")
@Builder
@Entity
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    @Embedded
    private UserAddress userAddress;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    public void addUser(User user) {
        this.user = user;
        user.setUserInfo(this);
    }
}