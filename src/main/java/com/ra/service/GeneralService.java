package com.ra.service;

import java.util.List;

public interface GeneralService<T> {
    List<T> listAll();
    T create(T object);
    T update(T newObject);
    boolean remove(String id);
    List<T> search(String any);
}
