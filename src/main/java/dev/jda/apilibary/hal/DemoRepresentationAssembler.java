package dev.jda.apilibary.hal;

import dev.jda.apilibary.controller.DemoController;
import dev.jda.apilibary.entity.Demo;

import dev.jda.demomodellibary.DemoDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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
