package com.mitocode.controller;

import com.mitocode.dto.ExamDTO;
import com.mitocode.exception.ModelNotFoundException;
import com.mitocode.model.Exam;
import com.mitocode.service.impl.ExamServiceImpl;
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
@RequestMapping("/exams")
public class ExamController {

    @Autowired
    private ExamServiceImpl service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<ExamDTO>> findAll(){
        List<ExamDTO> list = service.findAll().stream().map(p -> mapper.map(p, ExamDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamDTO> findById(@PathVariable("id") Integer id){
        return new ResponseEntity<ExamDTO>(mapper.map(getById(id), ExamDTO.class) , OK);
    }

//    @PostMapping
//    public ResponseEntity<Exam> save(@RequestBody Exam exam){
//        Exam obj = service.save(exam);
//        return new ResponseEntity<>(obj, CREATED);
//    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody ExamDTO dto){
        Exam obj = service.save(mapper.map(dto, Exam.class));
        //localhost:8080/exams/5
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getExamId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Exam> update(@RequestBody ExamDTO dto){
        Exam obj = service.update(mapper.map(dto, Exam.class));
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
    public EntityModel<ExamDTO> findByIdHateoas(@PathVariable("id") Integer id){
        EntityModel<ExamDTO> resource = EntityModel.of(mapper.map(getById(id), ExamDTO.class));
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).findById(id));
        resource.add(link.withRel("exam-info"));
        return resource;
    }

    private Exam getById(Integer id){
        Exam obj = service.findById(id);
        if (obj == null) {
            throw new ModelNotFoundException("EXAM ID NOT FOUND: " + id);
        }
        return obj;
    }

//    @GetMapping(produces = "application/xml")
//    @GetMapping
//    public Exam save(){
//        Exam exam = new Exam();
//        return service.save(exam);
//    }
}
