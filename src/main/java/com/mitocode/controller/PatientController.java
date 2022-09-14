package com.mitocode.controller;

import com.mitocode.exception.ModelNotFoundException;
import com.mitocode.model.Patient;
import com.mitocode.service.impl.PatientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientServiceImpl service;

    @GetMapping
    public ResponseEntity<List<Patient>> findAll(){
        List<Patient> list = service.findAll();
        return new ResponseEntity<>(list, OK);
    }

    private Patient getById(Integer id){
        Patient obj = service.findById(id);
        if (obj == null) {
            throw new ModelNotFoundException("PATIENT ID NOT FOUND: " + id);
        }
        return obj;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> findById(@PathVariable("id") Integer id){
        return new ResponseEntity<Patient>(getById(id), OK);
    }

//    @PostMapping
//    public ResponseEntity<Patient> save(@RequestBody Patient patient){
//        Patient obj = service.save(patient);
//        return new ResponseEntity<>(obj, CREATED);
//    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Patient patient){
        Patient obj = service.save(patient);
        //localhost:8080/patients/5
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getPatientId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Patient> update(@RequestBody Patient patient){
        Patient obj = service.update(patient);
        return new ResponseEntity<>(obj, OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        if (getById(id) != null) {
            service.delete(id);
        }
        return new ResponseEntity<>(NO_CONTENT);
    }



//    @GetMapping(produces = "application/xml")
//    @GetMapping
//    public Patient save(){
//        Patient patient = new Patient();
//        return service.save(patient);
//    }
}
