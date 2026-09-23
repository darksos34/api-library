package dev.jda.api.library.user;

import dev.jda.model.library.dto.UserDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "uuid", target = "uuid")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "name", target = "name")
    UserDTO toDto(User user);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "uuid", target = "uuid")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "name", target = "name")
    User toEntity(UserDTO dto);
}
