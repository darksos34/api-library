package dev.jda.api.library.service;

import dev.jda.api.library.config.ModelMapperConfiguration;
import dev.jda.api.library.entity.User;
import dev.jda.api.library.exception.GlobalExceptionHandler;
import dev.jda.api.library.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Import({ModelMapperConfiguration.class})
class UserServiceTest {

    public static final String USER_CODE = "ABCD";
    public static final String USER_UUID = "bc249d76-617a-4dfa-be47e7effeab8";

    @Mock
    protected UserRepository userRepository;

    private UserService unitToTest;

    @BeforeEach
    void beforeEachTest() {
        this.unitToTest = new UserService(userRepository);
    }

    @Test
    void testGetUserInfo() {
        when(userRepository.findByCode(any())).thenReturn(Optional.of(createUser()));
        User result = unitToTest.getUserByCode(USER_CODE);
        assertEquals(createUser(), result);
    }

    @Test
    void testGetUserByUuid() {
        User user = createUser();

        when(userRepository.findByUuid(USER_UUID)).thenReturn(Optional.of(user));

        User result = unitToTest.getUserByUuid(USER_UUID);

        assertEquals(user, result);
    }

    @Test
    void testGetAllUsersPageable_HappyPath() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<User> userPage = new PageImpl<>(Collections.nCopies(5, createUser()), pageable, 5);

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        assertEquals(userPage, unitToTest.getAllUserrsPageable(pageable));
    }

    @Test
    void testSaveUser_HappyPath() throws GlobalExceptionHandler.CodeExistsExceptionHandler {
        User user = createUser();

        when(userRepository.existsByCode(user.getCode())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = unitToTest.createUser(user);

        assertEquals(user.getCode(), result.getCode());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getUuid(), result.getUuid());
    }

    @Test
    void createUser_CodeExists() {
        User user = createUser();

        when(userRepository.existsByCode(user.getCode())).thenReturn(true);

        assertThrows(GlobalExceptionHandler.CodeExistsExceptionHandler.class, () -> unitToTest.createUser(user));

    }

    @Test
    void testCreateUserByUuid_EntityNotFound() {
        User newUser = createUser();

        when(userRepository.findByUuid(USER_UUID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> unitToTest.patchUserByUuid(USER_UUID, newUser));
    }

    @Test
    void testCreateUserByUuid_HappyPath_WithArgumentCaptor() {

        User existingUser = createUser();
        User newUser = User.builder()
                .code("ABCD")
                .name("henk")
                .uuid(USER_UUID)
                .build();

        when(userRepository.findByUuid(USER_UUID)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        User result = unitToTest.patchUserByUuid(USER_UUID, newUser);

        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals(newUser.getCode(), capturedUser.getCode());
        assertEquals(newUser.getName(), capturedUser.getName());
        assertEquals(newUser.getUuid(), capturedUser.getUuid());

        assertEquals(newUser.getCode(), result.getCode());
        assertEquals(newUser.getName(), result.getName());
        assertEquals(newUser.getUuid(), result.getUuid());
    }

    @Test
    void testCreateUserByUuid_NullFieldsInNewUser() {
        User existingUser = createUser();
        User newUser = User.builder()
                .code(null)
                .name(null)
                .uuid(null)
                .build();

        when(userRepository.findByUuid(USER_UUID)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = unitToTest.patchUserByUuid(USER_UUID, newUser);

        assertEquals(existingUser.getCode(), result.getCode());
        assertEquals(existingUser.getName(), result.getName());
        assertEquals(existingUser.getUuid(), result.getUuid());
    }


    @Test
    void shouldDeleteUserWhenUuidExists() {
        User user = createUser();

        when(userRepository.findByUuid(USER_UUID)).thenReturn(Optional.of(user));

        unitToTest.deleteUserByUuid(USER_UUID);

        verify(userRepository).delete(user);
    }

    private User createUser() {
        return User.builder()
                .code("1234")
                .name("henk")
                .uuid(USER_UUID)
                .build();
    }
}
