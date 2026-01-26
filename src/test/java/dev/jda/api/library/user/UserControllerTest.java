package dev.jda.api.library.user;

import dev.jda.api.library.common.mapper.ModelMapperConfiguration;
import dev.jda.api.library.profile.ProfileRepresentationAssembler;
import dev.jda.model.library.dto.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class,    properties = {
        "springdoc.api-docs.enabled=false",
        "springdoc.swagger-ui.enabled=false"
})
@Import({
        UserService.class, ModelMapperConfiguration.class, UserRepresentationAssembler.class, ProfileRepresentationAssembler.class
})
class UserControllerTest {

    @MockitoBean
    private ModelMapper modelMapper;

    @Getter
    @Setter
    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetUserByUuid_NotFound() throws Exception {
        when(userService.getUserByUuid(anyString()))
                .thenThrow(new EntityNotFoundException("User with uuid '%s' was not found"));

        mockMvc.perform(get("/v1/user/{uuid}", "missing-uuid")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 when invalid UserDTO is provided")
    void shouldReturn400WhenInvalidUserDtoIsProvided() throws Exception {
        mockMvc.perform(patch("/user/{uuid}", "test-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"uuid\":\"test-uuid\"}")) // missing name
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 404 when non-existing UUID is provided")
    void shouldReturn404WhenNonExistingUuidIsProvided() throws Exception {
        User user = new User();

        when(modelMapper.map(any(UserDTO.class), any())).thenReturn(user);
        when(userService.patchUserByUuid(anyString(), any(User.class))).thenReturn(null);

        mockMvc.perform(patch("/user/{uuid}", "non-existing-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"uuid\":\"test-uuid\", \"name\":\"test-name\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUserByCode() {

        doNothing().when(userService).deleteUserByUuid(createUser().getUuid());

        assertDoesNotThrow(() -> mockMvc.perform(delete("/v1/user/" + createUser().getUuid())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()));

        verify(userService, times(1)).deleteUserByUuid(createUser().getUuid());
    }

    @Test
    void shouldThrowExceptionWhenUuidDoesNotExist() throws Exception {

        doThrow(new EntityNotFoundException()).when(userService).deleteUserByUuid(createUser().getUuid());

        mockMvc.perform(delete("/v1/user/" +  createUser().getUuid())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).deleteUserByUuid(createUser().getUuid());
    }

    private User createUser(){
        return User.builder()
                .uuid("f3as5jj-8819-9952-b3ds-l0os8iwwejsa")
                .name("henk")
                .build();
    }
}
