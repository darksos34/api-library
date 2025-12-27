package dev.jda.api.library.controller;

import dev.jda.api.library.mapper.mapper.ModelMapperConfiguration;
import dev.jda.api.library.entity.Profile;
import dev.jda.api.library.hal.ProfileRepresentationAssembler;
import dev.jda.api.library.repository.ProfileRepository;
import dev.jda.api.library.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({ProfileService.class, ProfileController.class, ProfileRepresentationAssembler.class, ModelMapperConfiguration.class
})
@WebMvcTest(controllers = ProfileController.class,
        properties = {
                "springdoc.api-docs.enabled=false",
                "springdoc.swagger-ui.enabled=false"
        })
@ExtendWith(SpringExtension.class)
class ProfileControllerTest {

    @MockitoBean
    private ProfileRepository profileRepository;

    @MockitoBean
    private ProfileService unitToTest;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testDeleteProfileByUuid() throws Exception {

        doNothing().when(unitToTest).deleteProfileByUuid(createProfile().getUuid());

        mockMvc.perform(delete("/v1/profile/" + createProfile().getUuid())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private Profile createProfile() {
        return Profile.builder()
                .code("A1B2")
                .name("profile")
                .build();
    }
}
