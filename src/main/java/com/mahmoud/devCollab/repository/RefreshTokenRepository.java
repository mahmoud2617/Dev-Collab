package com.mahmoud.devCollab.repository;

import com.mahmoud.devCollab.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, UUID> {
    @EntityGraph(attributePaths = {"user"})
    Optional<RefreshToken> findByToken(String string);

    @Transactional
    @Modifying
    @Query("""
        DELETE FROM RefreshToken rt
        WHERE rt.token = :token""")
    void deleteByToken(@Param("token") String token);
}
