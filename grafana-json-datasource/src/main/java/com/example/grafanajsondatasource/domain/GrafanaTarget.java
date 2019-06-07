package com.example.grafanajsondatasource.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GrafanaTarget {
    String refId;
    String target;
    GrafanaTargetType type;
}
