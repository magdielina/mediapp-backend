package com.mitocode.controller;

import com.mitocode.dto.ConsultDTO;
import com.mitocode.dto.ConsultListExamDTO;
import com.mitocode.dto.ConsultProcDTO;
import com.mitocode.dto.FilterConsultDTO;
import com.mitocode.exception.ModelNotFoundException;
import com.mitocode.model.Consult;
import com.mitocode.model.Exam;
import com.mitocode.model.MediaFile;
import com.mitocode.service.IMediaFileService;
import com.mitocode.service.impl.ConsultServiceImpl;
import com.mitocode.service.impl.MediaFileServiceImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
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
    private IMediaFileService mfService;

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

    @PostMapping("/search/others")
    public ResponseEntity<List<ConsultDTO>> searchByOthers(@RequestBody FilterConsultDTO filterDTO) {
        List<Consult> consults = service.search(filterDTO.getDni(), filterDTO.getFullName());
        List<ConsultDTO> consultsDTO = mapper.map(consults, new TypeToken<List<ConsultDTO>>(){}.getType());
        return new ResponseEntity<>(consultsDTO, OK);
    }

    @GetMapping("/search/date")
    public ResponseEntity<List<ConsultDTO>> searchByDates(@RequestParam(value = "date1") String date1, @RequestParam(value = "date2") String date2){
        List<Consult> consults = service.searchByDates(LocalDateTime.parse(date1), LocalDateTime.parse(date2));
        List<ConsultDTO> consultsDTO = mapper.map(consults, new TypeToken<List<ConsultDTO>>(){}.getType());
        return new ResponseEntity<>(consultsDTO, OK);
    }

    @GetMapping("/callProcedure")
    public ResponseEntity<List<ConsultProcDTO>> callProcedureOrFunction(){
        List<ConsultProcDTO> consults = service.callProcedureOrFunction();
        return new ResponseEntity<>(consults, OK);
    }

    @GetMapping(value = "/generateReport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)//    @GetMapping(value = "/generateReport", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateReport() throws Exception{
        byte[] data = null;
        data = service.generateReport();
        return   new ResponseEntity<>(data, OK);
    }

    @PostMapping(value = "/saveFile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> saveFile(@RequestParam("file") MultipartFile file) throws Exception { //@RequestPart("medic") Medic medic
        MediaFile mf = new MediaFile();
        mf.setFileType(file.getContentType());
        mf.setFileName(file.getOriginalFilename());
        mf.setValue(file.getBytes());

        mfService.save(mf);

        return new ResponseEntity<>(OK);
    }




//    @GetMapping(produces = "application/xml")
//    @GetMapping
//    public Consult save(){
//        Consult consult = new Consult();
//        return service.save(consult);
//    }
}
