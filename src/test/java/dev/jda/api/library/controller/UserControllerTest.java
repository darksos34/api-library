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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@Import({
        ModelMapperConfiguration.class,
        UserRepresentationAssembler.class,
        ProfileRepresentationAssembler.class
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

    @Test
    void testCreateUser() throws Exception {
        // Arrange
        User mappedUser = User.builder()
                .code("ABCD")
                .name("henk")
                .build();

        User createdUser = createUser();

        when(modelMapper.map(any(UserDTO.class), eq(User.class))).thenReturn(mappedUser);
        when(unitToTest.createUser(any(User.class))).thenReturn(createdUser);

        when(userRepresentationAssembler.toModel(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            UserDTO dto = new UserDTO();
            dto.setUuid(u.getUuid());
            dto.setCode(u.getCode());
            dto.setName(u.getName());
            return dto;
        });

        // Act + Assert
        mockMvc.perform(post("/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code", is("ABCD")))
                .andExpect(jsonPath("$.name", is("henk")));

        verify(modelMapper).map(any(UserDTO.class), eq(User.class));
        verify(unitToTest).createUser(any(User.class));
    }

    @Test
    void testGetAllUserrsPageable_nonEmpty() throws Exception {
        var pageable = PageRequest.of(0, 2);
        var users = List.of(
                createUser(),
                User.builder().uuid("test-uuid-2").code("EFGH").name("piet").build()
        );
        Page<User> page = new PageImpl<>(users, pageable, users.size());

        when(unitToTest.getAllUserrsPageable(any(Pageable.class))).thenReturn(page);
        when(pagedResourcesAssembler.toModel(any(Page.class), eq(userRepresentationAssembler)))
                .thenReturn(PagedModel.empty());

        mockMvc.perform(get("/v1/user")
                        .param("page", "0")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(unitToTest, times(1)).getAllUserrsPageable(any(Pageable.class));
        verify(pagedResourcesAssembler, times(1)).toModel(any(Page.class), eq(userRepresentationAssembler));
    }

    @Test
    void testGetAllUserrsPageable_empty() throws Exception {
        var pageable = PageRequest.of(0, 2);
        Page<User> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(unitToTest.getAllUserrsPageable(any(Pageable.class))).thenReturn(emptyPage);
        when(pagedResourcesAssembler.toEmptyModel(any(Page.class), eq(UserDTO.class)))
                .thenReturn(PagedModel.empty());

        mockMvc.perform(get("/v1/user")
                        .param("page", "0")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(unitToTest, times(1)).getAllUserrsPageable(any(Pageable.class));
        verify(pagedResourcesAssembler, times(1)).toEmptyModel(any(Page.class), eq(UserDTO.class));
    }

    @Test
    void testPatchUserByUuid() throws Exception {
        // Arrange
        User mappedUser = User.builder()
                .code("ABCD")
                .name("henk")
                .build();

        User patched = createUser();

        when(modelMapper.map(any(UserDTO.class), eq(User.class))).thenReturn(mappedUser);
        when(unitToTest.patchUserByUuid(eq(patched.getUuid()), any(User.class))).thenReturn(patched);

        when(userRepresentationAssembler.toModel(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            UserDTO dto = new UserDTO();
            dto.setUuid(u.getUuid());
            dto.setCode(u.getCode());
            dto.setName(u.getName());
            return dto;
        });

        mockMvc.perform(patch("/v1/user/{uuid}", patched.getUuid())
                        .param("uuid", patched.getUuid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid", is(patched.getUuid())))
                .andExpect(jsonPath("$.name", is(patched.getName())))
                .andExpect(jsonPath("$.code", is(patched.getCode())));

        verify(modelMapper).map(any(UserDTO.class), eq(User.class));
        verify(unitToTest).patchUserByUuid(eq(patched.getUuid()), any(User.class));
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
                        patch("/v1/user/{uuid}", "test-uuid")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"uuid\":\"test-uuid\"}")) // missing name
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 404 when non-existing UUID is provided")
    void shouldReturn404WhenNonExistingUuidIsProvided() throws Exception {
        User user = new User();

        when(modelMapper.map(any(UserDTO.class), any())).thenReturn(user);
        when(unitToTest.patchUserByUuid(anyString(), any(User.class))).thenReturn(null);

        mockMvc.perform(
                        patch("/v1/user/{uuid}", "non-existing-uuid")
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
