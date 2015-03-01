package com.quester.experiment.dagger2experiment.persistence;

public interface DatabaseRepository<T> {

    T save(T element);

    T findOne(long id);

    void delete(long id);

    boolean exists(long id);
}
