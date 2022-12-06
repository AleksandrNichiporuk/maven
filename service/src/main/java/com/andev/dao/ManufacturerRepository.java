package com.andev.dao;

import com.andev.entity.Manufacturer;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;


@Repository
public class ManufacturerRepository extends RepositoryBase<Integer, Manufacturer> {

    public ManufacturerRepository(Session session) {
        super(Manufacturer.class, session);
    }
}
