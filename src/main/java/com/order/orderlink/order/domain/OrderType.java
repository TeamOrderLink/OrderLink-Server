package com.order.orderlink.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderType {
    ONLINE("온라인"), OFFLINE("오프라인");

    private final String value;
}
