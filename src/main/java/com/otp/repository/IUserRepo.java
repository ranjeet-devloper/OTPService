package com.otp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otp.model.User;

@Repository
public interface IUserRepo extends JpaRepository<User, Long> {

}
