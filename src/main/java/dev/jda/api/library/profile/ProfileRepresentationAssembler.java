package dev.jda.api.library.profile;

import dev.jda.model.library.dto.ProfileDTO;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
@NullMarked
public class ProfileRepresentationAssembler implements RepresentationModelAssembler<Profile, ProfileDTO> {

    private final ProfileMapper profileMapper;

    /**
     * @param profile the profile to convert
     * @return  the converted profile
     */
    @Override
    public ProfileDTO toModel(Profile profile) {
        ProfileDTO profileDTO = profileMapper.toDto(profile);
        addSelfLink(profileDTO);
        return profileDTO;
    }

    private void addSelfLink(ProfileDTO profileDTO) {
        Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(ProfileController.class).getProfileByUuid(profileDTO.getUuid())).withSelfRel();
        profileDTO.add(selfLink);
    }
}
