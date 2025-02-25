package me.catzy.invester.generic;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class GenericController<T, ID> {
    private final GenericService<T, ID> service;

    public GenericController(GenericService<T, ID> service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<T> create(@RequestBody T entity) throws Exception {
        return ResponseEntity.ok(service.save(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable ID id) {
		return service.findById(id).map(e->wrap(e)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
       return ResponseEntity.ok(wrap(service.findAll()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping({"{id}"})
    private ResponseEntity<Object> update(@PathVariable ID id, @RequestBody Map<Object, Object> list) throws Exception {
      return service.patch(id,list).map(e->wrap(e)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping
    private ResponseEntity<Object> updateMultiple(@RequestBody List<Object> list) throws Exception {
    	return service.patchMultiple(list).map(e->wrap(e)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    private Object wrap(List<T> obj) {
    	if(obj.size() == 0) {
    		return ResponseEntity.ok(obj);
    	}
    	T firstObj = obj.get(0);
		try {
			Class<?> viewClass = getJsonViewForEntity(firstObj.getClass());
			MappingJacksonValue wrapper = new MappingJacksonValue(obj);
			wrapper.setSerializationView(viewClass);

			return wrapper;
		} catch (RuntimeException e) {}
		return obj;
    }
    private Object wrap(T obj) {
		try {
			Class<?> viewClass = getJsonViewForEntity(obj.getClass());
			MappingJacksonValue wrapper = new MappingJacksonValue(obj);
			wrapper.setSerializationView(viewClass);

			return wrapper;
		} catch (RuntimeException e) {}
		return obj;
    }
    
    private Class<?> getJsonViewForEntity(Class<?> entityClass) {
        try {
            // Dynamically resolve the "values" interface for the entity
            return Class.forName(entityClass.getName() + "$values");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not resolve @JsonView interface for entity: " + entityClass.getName(), e);
        }
    }
}
