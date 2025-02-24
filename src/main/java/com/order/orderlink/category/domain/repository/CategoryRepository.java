package com.order.orderlink.category.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.orderlink.category.domain.Category;

public interface JpaCategoryRepository extends JpaRepository<Category, UUID> {
}
