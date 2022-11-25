package com.andev.util;

import com.andev.entity.Manufacturer;
import com.andev.entity.Product;
import com.andev.entity.enums.Category;
import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;

@UtilityClass
public class TestDataImporter {

    public void importData(SessionFactory sessionFactory) {
        @Cleanup Session session = sessionFactory.openSession();

        Manufacturer apple = saveManufacturer(session, "Apple");
        Manufacturer samsung = saveManufacturer(session, "Samsung");
        Manufacturer bosch = saveManufacturer(session, "Bosch");

        Product smartphone_IPhone11 = saveProduct(session, apple,
                "smartphone", "iPhone 11", Category.ELECTRONICS, BigDecimal.valueOf(850));
        Product smartphone_IPhone12 = saveProduct(session, apple,
                "smartphone", "iPhone 12", Category.ELECTRONICS, BigDecimal.valueOf(900));
        Product laptop_MacbookPro14 = saveProduct(session, apple,
                "laptop", "Macbook Pro 14", Category.COMPUTERS, BigDecimal.valueOf(2500));

        Product smartphone_GalaxyS22 = saveProduct(session, samsung,
                "smartphone", "Galaxy S22", Category.ELECTRONICS, BigDecimal.valueOf(750));
        Product monitor_S33 = saveProduct(session, samsung,
                "monitor", "S33", Category.COMPUTERS, BigDecimal.valueOf(200));

        Product fridge_Serie6VitaFresh = saveProduct(session, bosch,
                "fridge", "Serie 6 VitaFresh Plus", Category.HOME_TOOLS, BigDecimal.valueOf(2100));
    }

    private Manufacturer saveManufacturer(Session session, String name) {
        Manufacturer manufacturer = Manufacturer.builder()
                .name(name)
                .build();
        session.save(manufacturer);

        return manufacturer;
    }

    private Product saveProduct(
            Session session,
            Manufacturer manufacturer,
            String name,
            String model,
            Category category,
            BigDecimal price) {
        Product product = Product.builder()
                .manufacturer(manufacturer)
                .name(name)
                .model(model)
                .category(category)
                .price(price)
                .build();
        session.save(product);

        return product;
    }
}

