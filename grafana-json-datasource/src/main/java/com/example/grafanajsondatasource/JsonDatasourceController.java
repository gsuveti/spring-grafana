package com.example.grafanajsondatasource;

import com.example.grafanajsondatasource.domain.GrafanaQuery;
import com.example.grafanajsondatasource.domain.GrafanaTarget;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class JsonDatasourceController {
    @Value("classpath*:data/*.json")
    private Resource[] metrics;

    private ObjectMapper jsonMapper = new ObjectMapper();

    @GetMapping("/")
    public ResponseEntity testConnection() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/search")
    public ResponseEntity<List<String>> search() {
        List<String> metricNames = Arrays.stream(metrics)
                .map(Resource::getFilename)
                .collect(Collectors.toList());
        return ResponseEntity.ok(metricNames);
    }

    @PostMapping("/query")
    public ResponseEntity<List<Object>> query(@RequestBody GrafanaQuery query, Authentication authentication) throws IOException {
        List<String> requestedTargetNames = query.getTargets().stream()
                .map(GrafanaTarget::getTarget).collect(Collectors.toList());

        List<Object> metrics = Arrays.stream(this.metrics)
                .filter(metric -> requestedTargetNames.contains(metric.getFilename()))
                .map(metric -> {
                    try {
                        return metric.getFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .map(file -> {
                    try {
                        Map<String, Object> metric = new HashMap<>();
                        metric.put("target", Objects.requireNonNull(file).getName());
                        metric.put("datapoints", jsonMapper.readValue(file, Object.class));
                        return metric;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());


        return ResponseEntity.ok(metrics);
    }
}
