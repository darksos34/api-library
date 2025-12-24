package dev.jda.api.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jda.api.library.config.ModelMapperConfiguration;
import dev.jda.api.library.entity.Profile;
import dev.jda.api.library.hal.ProfileRepresentationAssembler;
import dev.jda.api.library.repository.ProfileRepository;
import dev.jda.api.library.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({ ProfileService.class, ProfileController.class, ProfileRepresentationAssembler.class, ModelMapperConfiguration.class
})
@WebMvcTest(ProfileController.class)
@ExtendWith(SpringExtension.class)
class ProfileControllerTest {

    public static final String PROFILE_JSON = "{\"uuid\":\"bc249d76-617a-4dfa-be47e7effeab8\", \"name\":\"profile\"}";

    @MockitoBean
    private ProfileRepository profileRepository;

    @MockitoBean
    private ProfileService unitToTest;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetProfileByUuid() throws Exception {
        when(unitToTest.getProfileByUuid(anyString())).thenReturn(createProfile());

        RequestBuilder request = MockMvcRequestBuilders
                .get("/v1/profile/bc249d76-617a-4dfa-be47e7effeab8")
                .accept(MediaType.APPLICATION_JSON)
                .content(PROFILE_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid", is("bc249d76-617a-4dfa-be47e7effeab8")))
                .andExpect(jsonPath("$.name", is("profile")));

        verify(unitToTest).getProfileByUuid(anyString());
    }

    @Test
    void testCreateProfile() throws Exception {
        when(unitToTest.createProfile(any(Profile.class))).thenReturn(createProfile());

        mockMvc.perform(post("/v1/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PROFILE_JSON))
                .andExpect(status().isCreated());

        verify(unitToTest, times(1)).createProfile(any(Profile.class));
    }

    @Test
    void testPutProfileByUuid() throws Exception {
        when(unitToTest.putProfileByUuid(anyString(), any())).thenReturn(createProfile());

        RequestBuilder request = MockMvcRequestBuilders
                .put("/v1/profile/bc249d76-617a-4dfa-be47e7effeab8")
                .accept(MediaType.APPLICATION_JSON)
                .content(PROFILE_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("profile")));

        verify(unitToTest).putProfileByUuid(anyString(), any());
    }

    @Test
    void testPatchProfileByUuid() throws Exception {
        when(unitToTest.patchProfileByUuid(anyString(), any())).thenReturn(createProfile());

        RequestBuilder request = MockMvcRequestBuilders
                .patch("/v1/profile/" + createProfile().getUuid())
                .accept(MediaType.APPLICATION_JSON)
                .content(PROFILE_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid", is("bc249d76-617a-4dfa-be47e7effeab8")))
                .andExpect(jsonPath("$.name", is("profile")));

        verify(unitToTest).patchProfileByUuid(anyString(), any());
    }

    @Test
    void testDeleteProfileByUuid() throws Exception {

        doNothing().when(unitToTest).deleteProfileByUuid(createProfile().getUuid());

        mockMvc.perform(delete("/v1/profile/" + createProfile().getUuid())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private Profile createProfile() {
        return Profile.builder()
                .uuid("bc249d76-617a-4dfa-be47e7effeab8")
                .name("profile")
                .build();
    }
}
