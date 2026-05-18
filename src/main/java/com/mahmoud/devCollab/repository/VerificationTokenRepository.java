package com.mahmoud.devCollab.repository;

import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.domain.entity.VerificationToken;
import com.mahmoud.devCollab.domain.enums.VerificationTokenType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, UUID> {
    Optional<VerificationToken> findByToken(String token);

    @Transactional
    void deleteAllByUserAndTokenType(User user, VerificationTokenType verificationTokenType);
}
