package com.order.orderlink.common.auth.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.AttributeConverter;

public class LocalTimeToStringConverter implements AttributeConverter<LocalTime, String> {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	@Override
	public String convertToDatabaseColumn(LocalTime localTime) {
		return (localTime == null) ? null : localTime.format(FORMATTER);
	}

	@Override
	public LocalTime convertToEntityAttribute(String dbData) {
		return (dbData == null || dbData.isEmpty()) ? null : LocalTime.parse(dbData, FORMATTER);
	}
}
