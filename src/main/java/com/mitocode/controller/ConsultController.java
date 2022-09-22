package com.mitocode.controller;

import com.mitocode.dto.ConsultDTO;
import com.mitocode.dto.ConsultListExamDTO;
import com.mitocode.exception.ModelNotFoundException;
import com.mitocode.model.Consult;
import com.mitocode.model.Exam;
import com.mitocode.service.impl.ConsultServiceImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
@RequestMapping("/consults")
public class ConsultController {

    @Autowired
    private ConsultServiceImpl service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<ConsultDTO>> findAll(){
        List<ConsultDTO> list = service.findAll().stream().map(p -> mapper.map(p, ConsultDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultDTO> findById(@PathVariable("id") Integer id){
        return new ResponseEntity<ConsultDTO>(mapper.map(getById(id), ConsultDTO.class) , OK);
    }

//    @PostMapping
//    public ResponseEntity<Consult> save(@RequestBody Consult consult){
//        Consult obj = service.save(consult);
//        return new ResponseEntity<>(obj, CREATED);
//    }

//    @PostMapping
//    public ResponseEntity<Void> save(@RequestBody ConsultDTO dto){
//        Consult obj = service.save(mapper.map(dto, Consult.class));
//        //localhost:8080/consults/5
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getConsultId()).toUri();
//        return ResponseEntity.created(location).build();
//    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody ConsultListExamDTO dto){
        Consult consult = mapper.map(dto.getConsult(), Consult.class);
        List<Exam> exams = mapper.map(dto.getLstExam(), new TypeToken<List<Exam>>(){}.getType());
        Consult obj = service.saveTransactional(consult, exams);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getConsultId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Consult> update(@RequestBody ConsultDTO dto){
        Consult obj = service.update(mapper.map(dto, Consult.class));
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
    public EntityModel<ConsultDTO> findByIdHateoas(@PathVariable("id") Integer id){
        EntityModel<ConsultDTO> resource = EntityModel.of(mapper.map(getById(id), ConsultDTO.class));
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).findById(id));
        resource.add(link.withRel("consult-info"));
        return resource;
    }

    private Consult getById(Integer id){
        Consult obj = service.findById(id);
        if (obj == null) {
            throw new ModelNotFoundException("CONSULT ID NOT FOUND: " + id);
        }
        return obj;
    }

//    @GetMapping(produces = "application/xml")
//    @GetMapping
//    public Consult save(){
//        Consult consult = new Consult();
//        return service.save(consult);
//    }
}