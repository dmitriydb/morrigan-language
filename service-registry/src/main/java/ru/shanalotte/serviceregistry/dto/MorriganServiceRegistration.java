package ru.shanalotte.serviceregistry.dto;

import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class MorriganServiceRegistration {
  String name;
  String host;
}
