package com.example.grafanajsondatasource.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Range;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class GrafanaQuery {
    String dashboardId;
    String panelId;
    String timezone;
    String interval;
    int intervalMs;
    int maxDataPoints;
    List<GrafanaTarget> targets;
    GrafanaRange range;
}
