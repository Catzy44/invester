package me.catzy.prestiz.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericServiceImpl<T, ID> implements GenericService<T, ID> {
    private final JpaRepository<T, ID> repository;
    @Autowired private ObjectMapper objectMapper;

    public GenericServiceImpl(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }
    
    @Override
    public Optional<T> patch(ID id, Map<Object, Object> map) throws JsonMappingException {
    	Optional<T> objOpt = repository.findById(id);
    	T obj = objOpt.get();
    	this.objectMapper.updateValue(obj, map);
    	
    	return Optional.of(save(obj));
	}
    
    @SuppressWarnings("unchecked")
    @Override
    public Optional<List<T>> patchMultiple(List<Object> list) throws JsonMappingException {
    	
    	List<T> modified = new ArrayList<T>();
    	for(Object mapObj : list) {
			Map<Object, Object> map = (Map<Object, Object>) mapObj;
			ID id = (ID) map.get("id");
			
			Optional<T> objOpt = repository.findById(id);
	    	T obj = objOpt.get();
	    	this.objectMapper.updateValue(obj, map);
	    	
	    	modified.add(save(obj));
    	}
    	
    	return Optional.of(modified);
    }
}