package com.mahmoud.devCollab.domain.entity;

import com.mahmoud.devCollab.domain.base.BaseUuidEntity;
import com.mahmoud.devCollab.domain.enums.VerificationTokenType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken extends BaseUuidEntity {
    @Column(name = "token")
    private String token;

    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private VerificationTokenType tokenType;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
