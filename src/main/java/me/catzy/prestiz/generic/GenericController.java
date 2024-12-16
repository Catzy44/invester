package me.catzy.prestiz.generic;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<T> getById(@PathVariable ID id) {
        return service.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<T>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping({"{id}"})
    private ResponseEntity<T> update(@PathVariable ID id, @RequestBody Map<Object, Object> list) throws Exception {
      return service.patch(id,list).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping
    private ResponseEntity<List<Object>> updateMultiple(@RequestBody List<Object> list) throws Exception {
    	return service.patchMultiple(list).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
