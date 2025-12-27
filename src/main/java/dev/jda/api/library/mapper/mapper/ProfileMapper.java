package dev.jda.api.library.mapper.mapper;

import dev.jda.api.library.entity.Profile;
import dev.jda.model.library.dto.ProfileDTO;
import org.modelmapper.ModelMapper;

import static org.modelmapper.Conditions.isNotNull;

public class ProfileMapper {

    private final ModelMapper mm = new ModelMapper();

    public ProfileMapper() {
        mm.createTypeMap(Profile.class, ProfileDTO.class)
                .addMappings(mapper -> {
                    mapper.when(isNotNull()).map(Profile::getUuid, ProfileDTO::setUuid);
                    mapper.when(isNotNull()).map(Profile::getCode, ProfileDTO::setCode);
                    mapper.when(isNotNull()).map(Profile::getName, ProfileDTO::setName);
                });

        mm.createTypeMap(ProfileDTO.class, Profile.class)
                .addMappings(mapper -> {
                    mapper.when(isNotNull()).map(ProfileDTO::getUuid, Profile::setUuid);
                    mapper.when(isNotNull()).map(ProfileDTO::getCode, Profile::setCode);
                    mapper.when(isNotNull()).map(ProfileDTO::getName, Profile::setName);
                });
    }

    public ProfileDTO toDto(Profile profile) {
        if (profile == null) return null;
        return mm.map(profile, ProfileDTO.class);
    }

    public Profile toEntity(ProfileDTO dto) {
        if (dto == null) return null;
        return mm.map(dto, Profile.class);
    }
}
