package com.mahmoud.devCollab.repository;

import com.mahmoud.devCollab.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :id")
    void deleteById(Long id);

    @Query(value = """
        SELECT u FROM User u
        JOIN FETCH u.profile p
        LEFT JOIN FETCH p.profilePicture pp
        LEFT JOIN FETCH u.userSkills us
        LEFT JOIN FETCH us.skill
        WHERE u.id = :id""")
    Optional<User> findByIdWithProfileAndSkills(Long id);

    @Query(value = """
        SELECT u FROM User u
        LEFT JOIN FETCH u.userSkills us
        LEFT JOIN FETCH us.skill
        WHERE u.id = :id""")
    Optional<User> findByIdWithSkills(Long id);
}
