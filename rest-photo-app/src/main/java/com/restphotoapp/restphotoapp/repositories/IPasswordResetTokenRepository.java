package com.restphotoapp.restphotoapp.repositories;

import com.restphotoapp.restphotoapp.models.DAL.PasswordResetTokenEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long> {
    PasswordResetTokenEntity findByToken(String token);
}