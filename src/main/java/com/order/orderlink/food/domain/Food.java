package com.order.orderlink.food.domain;

import com.order.orderlink.common.entity.BaseTimeEntity;
import com.order.orderlink.restaurant.domain.Restaurant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "p_foods")
public class Food extends BaseTimeEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_hidden", nullable = false)
    private boolean isHidden = false;

    public void updateFood(String name, String description, int price, String imageUrl, boolean isHidden) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isHidden = isHidden;
    }
}