package com.apis.repository;

import com.apis.model.AccessToken;
import com.apis.model.Deposit;
import com.apis.model.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, UUID> {

    Optional<AccessToken> findByTokenHashAndTokenType(String tokenHash, TokenType tokenType);

    Optional<AccessToken> findByDepositAndTokenType(Deposit deposit, TokenType tokenType);

    void deleteByDeposit(Deposit deposit);
}