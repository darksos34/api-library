package dev.jda.api.library.profile;

import dev.jda.model.library.dto.ProfileDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public ProfileDTO toDto(Profile profile) {
        if (profile == null) {
            return null;
        }
        ProfileDTO dto = new ProfileDTO();
        BeanUtils.copyProperties(profile, dto, "users", "user");
        return dto;
    }

    public Profile toEntity(ProfileDTO dto) {
        if (dto == null) {
            return null;
        }
        Profile profile = new Profile();
        BeanUtils.copyProperties(dto, profile, "users", "user");
        return profile;
    }
}
