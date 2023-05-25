package com.example.spring_mqtt_client.service;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.NeedsWaterDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class MqttService implements MqttCallback {

  private final MqttClient mqttClient;

  @Value("${mqtt.topic}")
  private String topic;

  private String needsWaterTopic = "needs_water";

  private int currentReconnectAttempt = 0;

  private final ObjectMapper objectMapper;

  public boolean needsWater = false;

  @PostConstruct
  public void init() throws MqttException {
    mqttClient.setCallback(this);
    mqttClient.subscribe(needsWaterTopic);
  }

  @Override
  public void connectionLost(Throwable cause) {
    log.error("Connection lost: {}", cause.getMessage());
    while (!mqttClient.isConnected()) {
      currentReconnectAttempt++;
      try {
        log.info("Reconnecting... (attempt {})", currentReconnectAttempt);
        mqttClient.reconnect();
        Thread.sleep(30000);
        log.info("MQTT connected: {}", mqttClient.isConnected());
      } catch (MqttException e) {
        log.error("Failed to reconnect: {}", e.getMessage());
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
    log.info("Successfully reconnected to the broker.");
    currentReconnectAttempt = 0;
    try {
      mqttClient.subscribe(needsWaterTopic);
    } catch (MqttException e) {
      log.error("Could not resubscribe to the topic {}", needsWaterTopic);
    }
  }

  @Override
  public void messageArrived(String topic, MqttMessage message) {
    try {
      NeedsWaterDto needsWaterDto = objectMapper.readValue(message.toString(), NeedsWaterDto.class);
      needsWater = needsWaterDto.isNeedsWater();
      log.info("Received message on topic " + topic + ": " + message.toString());
    } catch (Exception e) {
      log.error("Error parsing message: {}", e.getMessage());
    }
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken token) {
    // Not used in this example
  }

  public void publish(String topic, String message) throws MqttException {
    MqttMessage mqttMessage = new MqttMessage(message.getBytes());
    mqttMessage.setQos(1);
    mqttClient.publish(topic, mqttMessage);
  }
}
