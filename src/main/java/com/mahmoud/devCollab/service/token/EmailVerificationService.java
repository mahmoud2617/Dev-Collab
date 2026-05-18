package com.mahmoud.devCollab.service.token;

import com.mahmoud.devCollab.config.VerificationConfig;
import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.domain.entity.VerificationToken;
import com.mahmoud.devCollab.domain.enums.VerificationTokenType;
import com.mahmoud.devCollab.exception.InvalidRequestDataException;
import com.mahmoud.devCollab.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class EmailVerificationService implements VerificationService {
    private final TokenService tokenService;
    private final VerificationConfig verificationConfig;
    private final VerificationTokenRepository verificationTokenRepository;

    @Transactional
    @Override
    public String createToken(User user) {
        String token = tokenService.generateSecureToken();
        String hashedToken = tokenService.hashToken(token);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(hashedToken);
        verificationToken.setTokenType(VerificationTokenType.EMAIL_VERIFICATION);
        verificationToken.setExpirationDate(LocalDateTime.now().plusSeconds(verificationConfig.getEmailVerificationTokenExpiration()));

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    @Override
    public User validateToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(tokenService.hashToken(token))
                .orElse(null);

        if (verificationToken == null ||
            verificationToken.getExpirationDate().isBefore(LocalDateTime.now())
        ) {
            throw new InvalidRequestDataException("Invalid token parameter.");
        }

        return verificationToken.getUser();
    }

    @Override
    public void deleteTokens(User user) {
        verificationTokenRepository.deleteAllByUserAndTokenType(user, VerificationTokenType.EMAIL_VERIFICATION);
    }
}
