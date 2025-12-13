package dev.jda.api.library.controller;

import dev.jda.api.library.config.ModelMapperConfiguration;
import dev.jda.api.library.entity.User;
import dev.jda.api.library.hal.ProfileRepresentationAssembler;
import dev.jda.api.library.hal.UserRepresentationAssembler;
import dev.jda.api.library.repository.UserRepository;
import dev.jda.api.library.service.UserService;
import dev.jda.model.library.dto.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@Import({
        UserService.class, ModelMapperConfiguration.class, UserRepresentationAssembler.class, ProfileRepresentationAssembler.class
})
class UserControllerTest {

    public static final String USER_JSON = "{\"code\":\"ABCD\", \"name\":\"henk\"}";
    @MockitoBean
    private ModelMapper modelMapper;

    @Getter
    @Setter
    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserService unitToTest;

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserRepresentationAssembler userRepresentationAssembler;

    @MockitoBean
    private ProfileRepresentationAssembler profileRepresentationAssembler;

    @MockitoBean
    private PagedResourcesAssembler<User> pagedResourcesAssembler;

    @Test
    void testGetUserByCode() throws Exception {
        when(unitToTest.getUserByCode(anyString())).thenReturn(createUser());

        when(userRepresentationAssembler.toModel(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            UserDTO dto = new UserDTO();
            dto.setCode(u.getCode());
            dto.setName(u.getName());
            return dto;
        });

        mockMvc.perform(get("/v1/user/code/ABCD")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", Matchers.is("ABCD")))
                .andExpect(jsonPath("$.name", Matchers.is("henk")));

        verify(unitToTest).getUserByCode(anyString());
    }
    //!TODO getAllUserrsPageable, patchUserByUuid, createUser //

    @Test
    void testPatchUserByUuid() throws Exception {
        when(unitToTest.patchUserByUuid(anyString(), any())).thenReturn(createUser());

        RequestBuilder request = MockMvcRequestBuilders
                .patch("/v1/user/" + createUser().getUuid())
                .accept(MediaType.APPLICATION_JSON)
                .content(USER_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid", is("bc249d76-617a-4dfa-be47e7effeab8")))
                .andExpect(jsonPath("$.name", is("henk")));

        verify(unitToTest).patchUserByUuid(anyString(), any());
    }

    @Test
    void testGetUserByUuid_NotFound() throws Exception {
        when(unitToTest.getUserByUuid(anyString()))
                .thenThrow(new EntityNotFoundException("User with uuid '%s' was not found"));

        mockMvc.perform(
                        get("/v1/user/{uuid}", "missing-uuid")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 when invalid UserDTO is provided")
    void shouldReturn400WhenInvalidUserDtoIsProvided() throws Exception {
        mockMvc.perform(
                        patch("/user/{uuid}", "test-uuid")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"uuid\":\"test-uuid\"}")) // missing name
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 404 when non-existing UUID is provided")
    void shouldReturn404WhenNonExistingUuidIsProvided() throws Exception {
        User user = new User();

        when(modelMapper.map(any(UserDTO.class), any())).thenReturn(user);
        when(unitToTest.patchUserByUuid(anyString(), any(User.class))).thenReturn(null);

        mockMvc.perform(
                        patch("/user/{uuid}", "non-existing-uuid")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"uuid\":\"test-uuid\", \"name\":\"test-name\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUserByCode() {

        doNothing().when(unitToTest).deleteUserByUuid(createUser().getUuid());

        assertDoesNotThrow(() -> mockMvc.perform(delete("/v1/user/" + createUser().getUuid())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()));

        verify(unitToTest, times(1)).deleteUserByUuid(createUser().getUuid());
    }

    @Test
    void shouldThrowExceptionWhenUuidDoesNotExist() throws Exception {

        doThrow(new EntityNotFoundException()).when(unitToTest).deleteUserByUuid(createUser().getUuid());

        mockMvc.perform(delete("/v1/user/" +  createUser().getUuid())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(unitToTest, times(1)).deleteUserByUuid(createUser().getUuid());
    }

    private User createUser(){
        return User.builder()
                .uuid("f3as5jj-8819-9952-b3ds-l0os8iwwejsa")
                .code("ABCD")
                .name("henk")
                .build();
    }
}
