package com.recruit.springboot.RecruitmentWebPortal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());
        health.put("service", "RecruitmentWebPortal");
        return ResponseEntity.ok(health);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "RecruitmentWebPortal");
        info.put("version", "1.0.0");
        info.put("description", "Recruitment Web Portal API");
        info.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(info);
    }
}
