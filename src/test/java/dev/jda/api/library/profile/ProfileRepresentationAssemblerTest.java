package dev.jda.api.library.profile;

import dev.jda.model.library.dto.ProfileDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ExtendWith(MockitoExtension.class)
class ProfileRepresentationAssemblerTest {

    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private ProfileRepresentationAssembler unitToTest;

    private Profile profile;
    private ProfileDTO profileDTO;

    @BeforeEach
    void setUp() {
        profile = new Profile();
        profileDTO = new ProfileDTO();
    }

    @Test
    void toModelReturnsProfileDtoWithSelfLink() {
        when(profileMapper.toDto(profile)).thenReturn(profileDTO);

        ProfileDTO result = unitToTest.toModel(profile);

        verify(profileMapper).toDto(profile);
        assertEquals(profileDTO, result);
    }

    @Test
    void toModelAddsSelfLinkToProfileDto() {
        when(profileMapper.toDto(profile)).thenReturn(profileDTO);

        ProfileDTO result = unitToTest.toModel(profile);

        Link expectedLink = WebMvcLinkBuilder.linkTo(methodOn(ProfileController.class).getProfileByUuid(profileDTO.getCode())).withSelfRel();
        assertTrue(result.hasLink(expectedLink.getRel()));
        assertEquals(expectedLink, result.getLink(expectedLink.getRel()).get());
    }
}
