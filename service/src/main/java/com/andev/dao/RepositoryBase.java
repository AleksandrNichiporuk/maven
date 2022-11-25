package com.andev.dao;

import com.andev.entity.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class RepositoryBase<K extends Serializable, E extends BaseEntity<K>> implements Repository<K, E> {

    private final Class<E> clazz;
    private final Session session;

    @Override
    public E save(E entity) {
        session.save(entity);
        return entity;
    }

    @Override
    public void delete(E entity) {
        session.delete(entity);
        session.flush();
    }

    @Override
    public void update(E entity) {
        session.update(entity);
    }

    @Override
    public Optional<E> findById(K id) {
        return Optional.ofNullable(session.find(clazz, id));
    }

    @Override
    public List<E> findAll() {
        CriteriaQuery<E> criteria = session.getCriteriaBuilder().createQuery(clazz);
        criteria.from(clazz);
        return session.createQuery(criteria)
                .getResultList();
    }
}
