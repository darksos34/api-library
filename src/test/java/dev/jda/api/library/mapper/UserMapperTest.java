package dev.jda.api.library.mapper;

import dev.jda.api.library.entity.User;
import dev.jda.model.library.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

@Component
class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    void toDto_and_toEntity_mapAllFields_and_handleNulls() {
        User u = new User();
        u.setUuid("uuid-1");
        u.setCode("U123");
        u.setName("Alice");

        UserDTO dto = mapper.toDto(u);
        assertNotNull(dto);
        assertEquals(u.getUuid(), dto.getUuid());
        assertEquals(u.getCode(), dto.getCode());
        assertEquals(u.getName(), dto.getName());

        User mappedBack = mapper.toEntity(dto);
        assertNotNull(mappedBack);
        assertEquals(dto.getUuid(), mappedBack.getUuid());
        assertEquals(dto.getCode(), mappedBack.getCode());
        assertEquals(dto.getName(), mappedBack.getName());

        assertNull(mapper.toDto(null));
        assertNull(mapper.toEntity(null));
    }
}
