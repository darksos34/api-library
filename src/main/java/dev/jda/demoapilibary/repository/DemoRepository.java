package dev.jda.demoapilibary.repository;

import dev.jda.demoapilibary.entity.Demo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DemoRepository extends JpaRepository<Demo, String> {

    Optional<Demo> findByCode(String code);

    boolean existsByCode(String code);

}
