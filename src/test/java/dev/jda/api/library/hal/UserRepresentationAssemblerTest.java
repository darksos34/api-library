package dev.jda.api.library.hal;

import dev.jda.api.library.entity.Profile;
import dev.jda.api.library.entity.User;
import dev.jda.model.library.dto.ProfileDTO;
import dev.jda.model.library.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepresentationAssemblerTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ProfileRepresentationAssembler profileReprestoModel;

    @InjectMocks
    private UserRepresentationAssembler unitToTest;

    private User user;
    private UserDTO userDTO;
    private ProfileDTO profileDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        userDTO = new UserDTO();
        profileDTO = new ProfileDTO();

    }

    @Test
    void toModel_User_mapsAndAddsProfiles() {
        User createUser = User.builder()
                .code("ABCD")
                .name("testUser")
                .build();
        createUser.setProfiles(Collections.singletonList(new Profile()));

        userDTO.setCode("ABCD");
        when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenReturn(userDTO);
        when(profileReprestoModel.toModel(any(Profile.class))).thenReturn(profileDTO);

        UserDTO result = unitToTest.toModel(createUser);

        verify(modelMapper).map(createUser, UserDTO.class);
        verify(profileReprestoModel).toModel(any(Profile.class));
        assertEquals("ABCD", result.getCode());
        assertNotNull(result.getProfiles());
        assertEquals(1, result.getProfiles().size());
        assertEquals(Collections.singletonList(profileDTO), result.getProfiles());
    }

    @Test
    void toModelReturnsUserDtoWithSelfLink() {
        when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenReturn(new UserDTO());

        UserDTO result = unitToTest.toModel(user);

        verify(modelMapper).map(user, UserDTO.class);
        assertNotNull(result);
    }

    @Test
    void getProfilesAsModelReturnsEmptyListWhenProfilesIsNull() {
        user.setProfiles(null);

        assertEquals(Collections.emptyList(), unitToTest.getProfilesAsModel(user));
    }

    @Test
    void getProfilesAsModelReturnsProfileDtoListWhenProfilesIsNotNull() {
        user.setProfiles(Collections.singletonList(new Profile()));
        when(profileReprestoModel.toModel(any(Profile.class))).thenReturn(profileDTO);

        assertEquals(Collections.singletonList(profileDTO), unitToTest.getProfilesAsModel(user));
    }
}
