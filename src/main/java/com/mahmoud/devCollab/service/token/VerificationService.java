package com.mahmoud.devCollab.service.token;

import com.mahmoud.devCollab.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface VerificationService {
    String createToken(User user);

    User validateToken(String token);

    void deleteTokens(User user);
}
