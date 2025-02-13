package com.order.orderlink.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
