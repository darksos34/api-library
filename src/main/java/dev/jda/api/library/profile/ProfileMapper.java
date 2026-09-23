package dev.jda.api.library.profile;

import dev.jda.model.library.dto.ProfileDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.lang.reflect.Field;

@Mapper(componentModel = "spring")
public abstract class ProfileMapper {

    @BeanMapping(ignoreByDefault = true)
    public abstract ProfileDTO toDto(Profile profile);

    @BeanMapping(ignoreByDefault = true)
    public abstract Profile toEntity(ProfileDTO dto);

    @AfterMapping
    protected void fillDto(Profile source, @MappingTarget ProfileDTO target) {
        setField(target, "uuid", source.getUuid());
        setField(target, "code", source.getCode());
        setField(target, "name", source.getName());
    }

    @AfterMapping
    protected void fillEntity(ProfileDTO source, @MappingTarget Profile target) {
        target.setUuid((String) getField(source, "uuid"));
        target.setCode((String) getField(source, "code"));
        target.setName((String) getField(source, "name"));
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

    private static Object getField(Object source, String fieldName) {
        Field field = findField(source.getClass(), fieldName);
        try {
            field.setAccessible(true);
            return field.get(source);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("Unable to read field '" + fieldName + "'", ex);
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
