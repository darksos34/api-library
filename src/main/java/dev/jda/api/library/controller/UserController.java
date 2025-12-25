package dev.jda.api.library.controller;

import dev.jda.api.library.entity.User;
import dev.jda.api.library.exception.GlobalExceptionHandler.CodeExistsExceptionHandler;
import dev.jda.api.library.hal.UserRepresentationAssembler;
import dev.jda.api.library.service.UserService;
import dev.jda.model.library.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController implements UserApi {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserRepresentationAssembler userRepresentationAssembler;
    private final PagedResourcesAssembler<User> pagedResourcesAssembler;

    @Override
    public UserDTO getUserByCode(String code) {
        return userRepresentationAssembler.toModel(userService.getUserByCode(code));
    }

    @Override
    public UserDTO getUserByUuid(String uuid) {
        return userRepresentationAssembler.toModel(userService.getUserByUuid(uuid));
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public PagedModel<UserDTO> getAllUserrsPageable(Pageable pageable) {
        Page<User> userPage = userService.getAllUserrsPageable(pageable);
        if (!userPage.isEmpty()) {
            return (PagedModel<UserDTO>) pagedResourcesAssembler.toEmptyModel(userPage, UserDTO.class);
        }
        return pagedResourcesAssembler.toModel(userPage, userRepresentationAssembler);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) throws CodeExistsExceptionHandler {
        User user = modelMapper.map(userDTO, User.class);
        return userRepresentationAssembler.toModel(userService.createUser(user));
    }

    @Override
    public UserDTO patchUserByUuid(String uuid, UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        return userRepresentationAssembler.toModel(userService.patchUserByUuid(uuid, user));
    }

    @Override
    public void deleteUserByUuid(String uuid) {
        userService.deleteUserByUuid(uuid);
    }
}



