package com.mahmoud.devCollab.repository;

import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.domain.entity.VerificationToken;
import com.mahmoud.devCollab.domain.enums.VerificationTokenType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, UUID> {
    @EntityGraph(attributePaths = {"user"})
    Optional<VerificationToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("""
            DELETE FROM VerificationToken vt
            WHERE
                vt.user = :user AND
                vt.tokenType = :verificationTokenType""")
    void deleteAllByUserAndTokenType(
        @Param("user") User user,
        @Param("verificationTokenType") VerificationTokenType verificationTokenType
    );
}
