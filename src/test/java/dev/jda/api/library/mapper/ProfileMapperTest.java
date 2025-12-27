package dev.jda.api.library.mapper;

import dev.jda.api.library.entity.Profile;
import dev.jda.api.library.mapper.mapper.ProfileMapper;
import dev.jda.model.library.dto.ProfileDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileMapperTest {

    private final ProfileMapper mapper = new ProfileMapper();

    @Test
    void toDto_mapsAllFields() {
        Profile profile = createProfile();

        ProfileDTO dto = mapper.toDto(profile);

        assertNotNull(dto);
        assertEquals(profile.getUuid(), dto.getUuid());
        assertEquals(profile.getCode(), dto.getCode());
        assertEquals(profile.getName(), dto.getName());
    }

    @Test
    void toEntity_mapsAllFields() {
        ProfileDTO dto = createDto();

        Profile p = mapper.toEntity(dto);

        assertNotNull(p);
        assertEquals(dto.getUuid(), p.getUuid());
        assertEquals(dto.getCode(), p.getCode());
        assertEquals(dto.getName(), p.getName());
    }

    @Test
    void nullInput_returnsNull() {
        assertNull(mapper.toDto(null));
        assertNull(mapper.toEntity(null));
    }

    private Profile createProfile() {
        Profile p = new Profile();
        p.setUuid("uuid-1");
        p.setCode("CODE1");
        p.setName("Name 1");
        return p;
    }

    private ProfileDTO createDto() {
        ProfileDTO dto = new ProfileDTO();
        dto.setUuid("uuid-2");
        dto.setCode("CODE2");
        dto.setName("Name 2");
        return dto;
    }
}
