package dev.jda.api.library.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByCode(String code);

    Optional<User> findByUuid(String uuid);

    boolean existsByCode(String code);

}
