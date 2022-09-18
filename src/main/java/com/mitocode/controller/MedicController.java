package com.mitocode.controller;

import com.mitocode.dto.MedicDTO;
import com.mitocode.exception.ModelNotFoundException;
import com.mitocode.model.Medic;
import com.mitocode.service.impl.MedicServiceImpl;
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
@RequestMapping("/medics")
public class MedicController {

    @Autowired
    private MedicServiceImpl service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<MedicDTO>> findAll(){
        List<MedicDTO> list = service.findAll().stream().map(p -> mapper.map(p, MedicDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicDTO> findById(@PathVariable("id") Integer id){
        return new ResponseEntity<MedicDTO>(mapper.map(getById(id), MedicDTO.class) , OK);
    }

//    @PostMapping
//    public ResponseEntity<Medic> save(@RequestBody Medic medic){
//        Medic obj = service.save(medic);
//        return new ResponseEntity<>(obj, CREATED);
//    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody MedicDTO dto){
        Medic obj = service.save(mapper.map(dto, Medic.class));
        //localhost:8080/medics/5
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getMedicId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Medic> update(@RequestBody MedicDTO dto){
        Medic obj = service.update(mapper.map(dto, Medic.class));
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
    public EntityModel<MedicDTO> findByIdHateoas(@PathVariable("id") Integer id){
        EntityModel<MedicDTO> resource = EntityModel.of(mapper.map(getById(id), MedicDTO.class));
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).findById(id));
        resource.add(link.withRel("medic-info"));
        return resource;
    }

    private Medic getById(Integer id){
        Medic obj = service.findById(id);
        if (obj == null) {
            throw new ModelNotFoundException("MEDIC ID NOT FOUND: " + id);
        }
        return obj;
    }

//    @GetMapping(produces = "application/xml")
//    @GetMapping
//    public Medic save(){
//        Medic medic = new Medic();
//        return service.save(medic);
//    }
}
