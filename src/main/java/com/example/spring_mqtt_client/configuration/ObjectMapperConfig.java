package com.example.spring_mqtt_client.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class ObjectMapperConfig {
  @Bean
  public ObjectMapper objectMapper() {
    SimpleModule doubleModule = new SimpleModule();
    SimpleModule localDateModule = new SimpleModule();
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper
        .registerModule(doubleModule)
        .registerModule(new JavaTimeModule())
        .registerModule(localDateModule);
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    return objectMapper;
  }
}
