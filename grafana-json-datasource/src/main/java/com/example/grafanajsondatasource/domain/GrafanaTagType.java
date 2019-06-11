package com.example.grafanajsondatasource.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GrafanaTagType {
    @JsonProperty("string")
    STRING,

    @JsonProperty("number")
    NUMBER;
}
