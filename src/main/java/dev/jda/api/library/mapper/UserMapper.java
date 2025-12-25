package dev.jda.api.library.mapper;

import dev.jda.api.library.entity.User;
import dev.jda.model.library.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import static org.modelmapper.Conditions.isNotNull;

@Component
public class UserMapper {

    private final ModelMapper mm = new ModelMapper();

    public UserMapper() {
        mm.createTypeMap(User.class, UserDTO.class)
                .addMappings(mapper -> {
                    mapper.when(isNotNull()).map(User::getUuid, UserDTO::setUuid);
                    mapper.when(isNotNull()).map(User::getCode, UserDTO::setCode);
                    mapper.when(isNotNull()).map(User::getName, UserDTO::setName);
                });

        mm.createTypeMap(UserDTO.class, User.class)
                .addMappings(mapper -> {
                    mapper.when(isNotNull()).map(UserDTO::getUuid, User::setUuid);
                    mapper.when(isNotNull()).map(UserDTO::getCode, User::setCode);
                    mapper.when(isNotNull()).map(UserDTO::getName, User::setName);
                });
    }

    public UserDTO toDto(User user) {
        if (user == null) return null;
        return mm.map(user, UserDTO.class);
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;
        return mm.map(dto, User.class);
    }
}
