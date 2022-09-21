package com.mitocode.controller;

import com.mitocode.dto.SpecialtyDTO;
import com.mitocode.exception.ModelNotFoundException;
import com.mitocode.model.Specialty;
import com.mitocode.service.impl.SpecialtyServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/specialties")
public class SpecialtyController {

    @Autowired
    private SpecialtyServiceImpl service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<SpecialtyDTO>> findAll(){
        List<SpecialtyDTO> list = service.findAll().stream().map(p -> mapper.map(p, SpecialtyDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyDTO> findById(@PathVariable("id") Integer id){
        return new ResponseEntity<SpecialtyDTO>(mapper.map(getById(id), SpecialtyDTO.class) , OK);
    }

//    @PostMapping
//    public ResponseEntity<Specialty> save(@RequestBody Specialty specialty){
//        Specialty obj = service.save(specialty);
//        return new ResponseEntity<>(obj, CREATED);
//    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody SpecialtyDTO dto){
        Specialty obj = service.save(mapper.map(dto, Specialty.class));
        //localhost:8080/specialties/5
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getSpecialtyId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Specialty> update(@RequestBody SpecialtyDTO dto){
        Specialty obj = service.update(mapper.map(dto, Specialty.class));
        return new ResponseEntity<>(obj, OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        if (getById(id) != null) {
            service.delete(id);
        }
        return new ResponseEntity<>(NO_CONTENT);
    }

    @GetMapping("/hateoas/{id}")
    public EntityModel<SpecialtyDTO> findByIdHateoas(@PathVariable("id") Integer id){
        EntityModel<SpecialtyDTO> resource = EntityModel.of(mapper.map(getById(id), SpecialtyDTO.class));
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).findById(id));
        resource.add(link.withRel("specialty-info"));
        return resource;
    }

    private Specialty getById(Integer id){
        Specialty obj = service.findById(id);
        if (obj == null) {
            throw new ModelNotFoundException("SPECIALTY ID NOT FOUND: " + id);
        }
        return obj;
    }

//    @GetMapping(produces = "application/xml")
//    @GetMapping
//    public Specialty save(){
//        Specialty specialty = new Specialty();
//        return service.save(specialty);
//    }
}
