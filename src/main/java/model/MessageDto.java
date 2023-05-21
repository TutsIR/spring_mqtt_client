package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {

  @JsonProperty("gpio_pin")
  private int gpioPin;

  private boolean activated;
}
