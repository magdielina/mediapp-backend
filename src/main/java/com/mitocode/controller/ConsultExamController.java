package com.mitocode.controller;

import com.mitocode.dto.ConsultExamDTO;
import com.mitocode.model.ConsultExam;
import com.mitocode.service.IConsultExamService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/consultexams")
public class ConsultExamController {

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private IConsultExamService service;

    @GetMapping("/{consultId}")
    public ResponseEntity<List<ConsultExamDTO>> getExamsByConsultId(@PathVariable("consultId") Integer consultId){
        List<ConsultExam> consultExams = service.getExamsByConsultId(consultId);
        List<ConsultExamDTO> lstDTO = mapper.map(consultExams, new TypeToken<List<ConsultExamDTO>>(){}.getType());
        return new ResponseEntity<>(lstDTO, HttpStatus.OK);
    }
}
