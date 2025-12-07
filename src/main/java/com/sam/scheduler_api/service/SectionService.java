package com.sam.scheduler_api.service;

import com.sam.scheduler_api.model.Section;
import com.sam.scheduler_api.repository.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public List<Section> findAllSections() {
        return sectionRepository.findAll();
    }

}


