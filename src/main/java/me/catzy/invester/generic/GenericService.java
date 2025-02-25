package me.catzy.invester.generic;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonMappingException;

public interface GenericService<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
    Optional<T> patch(ID id, Map<Object, Object> list) throws JsonMappingException;
    Optional<List<T>> patchMultiple(List<Object> list) throws JsonMappingException;
}