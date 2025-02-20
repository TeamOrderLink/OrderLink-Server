package com.order.orderlink.region.domain;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.order.orderlink.common.entity.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "p_regions")
@EntityListeners(AuditingEntityListener.class)
public class Region extends BaseTimeEntity {
	@Id
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	private UUID id;

	@NotBlank
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Region parent; // 부모(상위) 지역

	public void updateName(String name) {
		this.name = name;
	}

	public void updateParent(Region parent) {
		this.parent = parent;
	}

}
