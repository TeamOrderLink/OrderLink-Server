package com.order.orderlink.ai.domain;

import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.order.orderlink.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_ai_api_log")
@SQLRestriction("deleted_at IS NULL")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiApiLog extends BaseTimeEntity {

	@Id
	@GeneratedValue
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "request_text", columnDefinition = "text", nullable = false)
	private String requestText;

	@Column(name = "response_text", columnDefinition = "text", nullable = false)
	private String responseText;

	@Builder
	public AiApiLog(UUID userId, String requestText, String responseText) {
		this.userId = userId;
		this.requestText = requestText;
		this.responseText = responseText;
	}
}
