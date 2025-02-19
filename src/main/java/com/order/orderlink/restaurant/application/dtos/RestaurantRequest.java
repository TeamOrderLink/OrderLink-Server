package com.order.orderlink.restaurant.application.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class RestaurantRequest {

    @Getter
    public static class Create {
        @NotBlank
        private String name;

        @NotBlank
        private String address;

        @Pattern(regexp = "^[0-9]{10,11}$", message = "-를 제외한 10자리 또는 11자리 숫자로 입력해주세요.")
        @NotBlank
        private String phone;

        private String description;

        @NotBlank
        @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "영업 시간은 00:00 ~ 23:59까지만 입력 가능합니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime openTime;

        @NotBlank
        @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "영업 시간은 00:00 ~ 23:59까지만 입력 가능합니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime closeTime;

        @NotBlank
        private String ownerName;

        @NotBlank
        @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}$", message = "사업자 등록 번호는 '-'를 포함하여 000-00-00000 형식으로 입력해야 합니다.")
        private String businessRegNum;

        @NotBlank
        @Pattern(regexp = "^[가-힣]+$", message = "카테고리는 한글만 입력 가능합니다.")
        private String categories;

        @NotBlank
        @Pattern(regexp = "^[가-힣]+$", message = "지역명은 한글만 입력 가능합니다.")
        private String region;

        @NotBlank
        private List<RestaurantFoodDto> foods;
    }
}
