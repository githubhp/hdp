package com.br.mom.ms.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonProcessUtil {
	private static volatile ObjectMapper objectMapper;

	public static ObjectMapper getMapperInstance() {
		if (objectMapper == null) {

			synchronized (ObjectMapper.class) {

				if (objectMapper == null) {

					objectMapper = new ObjectMapper();

				}

			}

		}
		return objectMapper;

	}
}