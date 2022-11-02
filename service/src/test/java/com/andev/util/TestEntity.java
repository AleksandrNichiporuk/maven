package com.andev.util;

import com.andev.entity.Manufacturer;
import com.andev.entity.Order;
import com.andev.entity.Product;
import com.andev.entity.User;
import com.andev.entity.UserAddress;
import com.andev.entity.UserInfo;
import com.andev.entity.enums.Category;
import com.andev.entity.enums.Payment;
import com.andev.entity.enums.Role;
import com.andev.entity.enums.Status;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;

@UtilityClass
public class TestEntity {
    public static User getTestUser() {
        return User.builder()
                .login("Ivan")
                .password("333")
                .role(Role.CUSTOMER)
                .build();
    }

    public static UserInfo getTestUserInfo() {
        return UserInfo.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("ivan@gmail.com")
                .phone("7777777")
                .userAddress(UserAddress.builder()
                        .town("Minsk")
                        .street("Kolasa")
                        .houseNumber(15)
                        .apartmentNumber(3)
                        .postalCode(220015)
                        .build())
                .build();
    }

    public static Order getFirstTestOrder() {
        return Order.builder()
                .dateOrder(LocalDate.of(2022, 9, 23))
                .dateClosing(LocalDate.of(2022, 9, 27))
                .payment(Payment.CASH)
                .totalValue(150)
                .status(Status.ARRIVED)
                .build();
    }

    public static Order getSecondTestOrder() {
        return Order.builder()
                .dateOrder(LocalDate.of(2022, 10, 12))
                .dateClosing(LocalDate.of(2022, 10, 15))
                .payment(Payment.CARD)
                .totalValue(195)
                .status(Status.ARRIVED)
                .build();
    }

    public static Product getTestProduct() {
        return Product.builder()
                .name("cellphone")
                .model("iPhone 14")
                .category(Category.ELECTRONICS)
                .description("test description")
                .price(BigDecimal.valueOf(1300))
                .amount(33)
                .build();
    }

    public static Manufacturer getTestManufacturer() {
        return Manufacturer.builder()
                .name("Apple")
                .description("test description")
                .build();
    }
}


