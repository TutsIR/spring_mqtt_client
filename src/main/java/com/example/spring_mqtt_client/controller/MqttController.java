package com.example.spring_mqtt_client.controller;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.example.spring_mqtt_client.service.MqttService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import model.MessageDto;
import model.NeedsWaterDto;

@RestController
@RequestMapping("/mqtt")
@RequiredArgsConstructor
public class MqttController {

  @Value("${mqtt.topic}")
  private String topic;

  private final MqttService mqttService;

  private final ObjectMapper objectMapper;

  @PostMapping("/publish")
  public void publish(@RequestBody MessageDto message)
      throws MqttException, JsonProcessingException {

    String messageString = objectMapper.writeValueAsString(message);
    mqttService.publish(topic, messageString);
  }

  @GetMapping("/needs-water")
  public boolean getWateringStatus() {
    return mqttService.needsWater;
  }
}
