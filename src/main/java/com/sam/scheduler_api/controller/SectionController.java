package com.sam.scheduler_api.controller;

import com.sam.scheduler_api.model.Section;
import com.sam.scheduler_api.repository.SectionRepository;
import com.sam.scheduler_api.service.SectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping
    public List<Section> getAllSections() {
        return sectionService.findAllSections();
    }

}
