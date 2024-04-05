package dev.jda.apilibary.service;

import dev.jda.apilibary.entity.Demo;
import dev.jda.apilibary.repository.DemoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemoService {

    private static final String DEMO_NOTFOUND = "Kon de demo niet vinden op basis van UUID";
    private final DemoRepository demoRepository;

    public Demo getDemoByCode(String code) {
        return demoRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Demo met code '%s' is niet gevonden", code)));
    }

    public Demo saveDemo(Demo demo) {
        if (demoRepository.existsByCode(demo.getCode())) {
            throw new RuntimeException(String.format("Code '%s' bestaat al.", demo.getCode()));
        }
        return demoRepository.save(demo);
    }

    public Page<Demo> getAllDemosPageable(Pageable pageable) {
        return demoRepository.findAll(pageable);
    }

    public Demo saveDemoByUuid(Demo demoDTO) {
        Demo existingDemo = demoRepository.findById(demoDTO.getUuid())
                .orElseThrow(() -> new EntityNotFoundException(DEMO_NOTFOUND));
        existingDemo.setCode(demoDTO.getCode());
        existingDemo.setName(demoDTO.getName());
        existingDemo.setUuid(demoDTO.getUuid());
        return demoRepository.save(existingDemo);
    }
}
