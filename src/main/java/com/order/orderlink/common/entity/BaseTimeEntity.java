package com.order.orderlink.common.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@CreatedBy
	@Column(name = "created_by", updatable = false)
	private String createdBy;

	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@LastModifiedBy
	@Column(name = "updated_by")
	private String updatedBy;

	// Soft Delete를 위한 필드
	@Setter
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Setter
	@Column(name = "deleted_by")
	private String deletedBy;

	// Soft Delete 메소드
	public void softDelete(String deletedBy) {
		this.deletedAt = LocalDateTime.now();
		this.deletedBy = deletedBy;
	}

}
