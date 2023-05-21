package com.example.spring_mqtt_client.service;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MqttService implements MqttCallback {

  private final MqttClient mqttClient;

  @Value("${mqtt.topic}")
  private String topic;

  private int currentReconnectAttempt = 0;

  @PostConstruct
  public void init() throws MqttException {
    mqttClient.setCallback(this);
    mqttClient.subscribe(topic);
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
      mqttClient.subscribe(topic);
    } catch (MqttException e) {
      log.error("Could not resubscribe to the topic {}", topic);
    }
  }

  @Override
  public void messageArrived(String topic, MqttMessage message) {
    try {
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
