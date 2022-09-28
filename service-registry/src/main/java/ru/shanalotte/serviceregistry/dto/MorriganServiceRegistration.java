package ru.shanalotte.serviceregistry.dto;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
@RequiredArgsConstructor
public class MorriganServiceRegistration {
  String name;
  String host;
}
