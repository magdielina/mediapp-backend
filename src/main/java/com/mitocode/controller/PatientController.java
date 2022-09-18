package com.mitocode.controller;

import com.mitocode.dto.PatientDTO;
import com.mitocode.exception.ModelNotFoundException;
import com.mitocode.model.Patient;
import com.mitocode.service.impl.PatientServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientServiceImpl service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<PatientDTO>> findAll(){
        List<PatientDTO> list = service.findAll().stream().map(p -> mapper.map(p, PatientDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> findById(@PathVariable("id") Integer id){
        return new ResponseEntity<PatientDTO>(mapper.map(getById(id), PatientDTO.class) , OK);
    }

//    @PostMapping
//    public ResponseEntity<Patient> save(@RequestBody Patient patient){
//        Patient obj = service.save(patient);
//        return new ResponseEntity<>(obj, CREATED);
//    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody PatientDTO dto){
        Patient obj = service.save(mapper.map(dto, Patient.class));
        //localhost:8080/patients/5
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getPatientId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Patient> update(@RequestBody PatientDTO dto){
        Patient obj = service.update(mapper.map(dto, Patient.class));
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
    public EntityModel<PatientDTO> findByIdHateoas(@PathVariable("id") Integer id){
        EntityModel<PatientDTO> resource = EntityModel.of(mapper.map(getById(id), PatientDTO.class));
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).findById(id));
        resource.add(link.withRel("patient-info"));
        return resource;
    }

    private Patient getById(Integer id){
        Patient obj = service.findById(id);
        if (obj == null) {
            throw new ModelNotFoundException("PATIENT ID NOT FOUND: " + id);
        }
        return obj;
    }

//    @GetMapping(produces = "application/xml")
//    @GetMapping
//    public Patient save(){
//        Patient patient = new Patient();
//        return service.save(patient);
//    }
}
