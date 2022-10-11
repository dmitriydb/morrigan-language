package ru.shanalotte.serviceregistry.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@RequiredArgsConstructor
@Getter
@ToString
public class KnownService {
  String name;
  int port;
  String host;
}
