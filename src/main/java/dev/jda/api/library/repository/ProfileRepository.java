package dev.jda.api.library.repository;

import dev.jda.api.library.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {

    Optional<Profile> findByUuid(String uuid);
}
