package com.glowapex.controller;

import com.glowapex.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // ✅ Accessible only by SUPER_ADMIN
    @GetMapping("/{type}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    public Map<String, Object> getReport(@PathVariable String type) {
        return reportService.getReport(type);
    }
}