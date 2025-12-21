package com.sam.scheduler_api.service;

import com.sam.scheduler_api.model.Professor;
import com.sam.scheduler_api.repository.ProfessorRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    // Wrapper method to save a professor
    public Professor saveProfessor(Professor professor) {
        return professorRepository.save(professor);
    }















}
