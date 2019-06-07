package com.example.grafanajsondatasource.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@Setter
@Getter
public class GrafanaRange {
    Instant from;
    Instant to;
}
