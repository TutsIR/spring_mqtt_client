package com.example.spring_mqtt_client.configuration;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

  @Value("${mqtt.broker.url}")
  private String brokerUrl;

  @Value("${mqtt.broker.username}")
  private String username;

  @Value("${mqtt.broker.password}")
  private String password;

  @Value("${mqtt.clientId}")
  private String clientId;

  @Bean
  public MqttClient mqttClient() throws Exception {
    MqttConnectOptions options = new MqttConnectOptions();
    options.setUserName(username);
    options.setPassword(password.toCharArray());
    options.setCleanSession(true);
    options.setConnectionTimeout(30);
    options.setKeepAliveInterval(60);
    options.setAutomaticReconnect(true);

    MqttClient mqttClient = new MqttClient(brokerUrl, clientId, new MemoryPersistence());
    mqttClient.connect(options);

    return mqttClient;
  }
}
