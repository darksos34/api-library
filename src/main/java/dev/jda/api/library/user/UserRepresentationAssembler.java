package dev.jda.api.library.user;

import dev.jda.api.library.profile.ProfileRepresentationAssembler;
import dev.jda.model.library.dto.ProfileDTO;
import dev.jda.model.library.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
@NullMarked
public class UserRepresentationAssembler implements RepresentationModelAssembler<User, UserDTO> {

    private final UserMapper userMapper;
    private final ProfileRepresentationAssembler profileReprestoModel;

    @Override
    public @NonNull UserDTO toModel(@NonNull User entity) {
        UserDTO userDTO = userMapper.toDto(entity);
        addSelfLink(userDTO, entity.getUuid());
        setField(userDTO, "profiles", getProfilesAsModel(entity));
        return userDTO;
    }

    public List<ProfileDTO> getProfilesAsModel(User entity) {
        if(entity.getProfiles() == null) return Collections.emptyList();
        return entity.getProfiles().stream()
                .map(profileReprestoModel::toModel)
                .toList();
    }

    private void addSelfLink(UserDTO userDTO, String uuid) {
        Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserByUuid(uuid)).withSelfRel();
        userDTO.add(selfLink);
    }

    private static void setField(Object target, String fieldName, Object value) {
        Field field = findField(target.getClass(), fieldName);
        try {
            field.setAccessible(true);
            field.set(target, value);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("Unable to set field '" + fieldName + "'", ex);
        }
    }

    private static Field findField(Class<?> type, String fieldName) {
        Class<?> current = type;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        throw new IllegalStateException("Field '" + fieldName + "' not found on " + type.getName());
    }
}
