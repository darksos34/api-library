package dev.jda.demoapilibary.hal;

import dev.jda.demoapilibary.controller.DemoController;
import dev.jda.demoapilibary.entity.Demo;
import dev.jda.demomodellibary.DemoDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
@Component
@RequiredArgsConstructor
public class DemoRepresentationAssembler implements RepresentationModelAssembler<Demo, DemoDTO> {

    private final ModelMapper modelMapper;

    @Override
    public DemoDTO toModel(Demo demo) {
        DemoDTO demoDTO = modelMapper.map(demo, DemoDTO.class);
        addSelfLink(demoDTO);
        return demoDTO;
    }

    private void addSelfLink(DemoDTO demoDTO) {
        Link selfLink = linkTo(methodOn(DemoController.class).getDemoByCode(demoDTO.getCode())).withSelfRel();
        demoDTO.add(selfLink);
    }
}
