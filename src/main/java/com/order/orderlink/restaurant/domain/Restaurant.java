package com.order.orderlink.restaurant.domain;

import com.order.orderlink.common.auth.util.LocalTimeToStringConverter;
import com.order.orderlink.common.entity.BaseTimeEntity;
import com.order.orderlink.food.domain.Food;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "p_restaurants")
@EntityListeners(AuditingEntityListener.class)
public class Restaurant extends BaseTimeEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", unique = true, nullable = false)
    private String address;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "description")
    private String description;

    @Convert(converter = LocalTimeToStringConverter.class)
    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;

    @Convert(converter = LocalTimeToStringConverter.class)
    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;

    @Builder.Default
    @Column(name = "business_status", nullable = false)
    private boolean businessStatus = true;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "business_reg_num", unique = true, nullable = false)
    private String businessRegNum;

    @Builder.Default
    @Column(name = "avg_rating", nullable = false)
    private Double avgRating = 0.0;

    @Builder.Default
    @Column(name = "rating_sum", nullable = false)
    private Double ratingSum = 0.0;

    @Builder.Default
    @Column(name = "rating_count", nullable = false)
    private Integer ratingCount = 0;

    @OneToMany(mappedBy = "restaurant")
    private List<Food> foods = new ArrayList<>();
}