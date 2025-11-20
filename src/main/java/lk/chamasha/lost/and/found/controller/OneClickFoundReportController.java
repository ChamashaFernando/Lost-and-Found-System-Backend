package lk.chamasha.lost.and.found.controller;//package lk.chamasha.lost.and.found.controller;
//
//import lk.chamasha.lost.and.found.controller.request.OneClickFoundReportRequest;
//import lk.chamasha.lost.and.found.controller.response.OneClickFoundReportResponse;
//import lk.chamasha.lost.and.found.exception.ReportNotFoundException;
//import lk.chamasha.lost.and.found.exception.UserNotFoundException;
//import lk.chamasha.lost.and.found.service.OneClickFoundReportService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/found")
//@RequiredArgsConstructor
//public class OneClickFoundReportController {
//
//    private final OneClickFoundReportService reportService;
//
//    // Report a found item
//    @PostMapping("/report")
//    public ResponseEntity<OneClickFoundReportResponse> reportFound(@RequestBody OneClickFoundReportRequest request)
//            throws UserNotFoundException {
//        OneClickFoundReportResponse response = reportService.reportFound(request);
//        return ResponseEntity.ok(response);
//    }
//
//    // Get all found reports submitted by a specific user
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<OneClickFoundReportResponse>> getReportsByUser(@PathVariable Long userId)
//            throws UserNotFoundException, ReportNotFoundException {
//        List<OneClickFoundReportResponse> reports = reportService.getReportsByUser(userId);
//        return ResponseEntity.ok(reports);
//
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<List<OneClickFoundReportResponse>> getAllReports() throws ReportNotFoundException {
//        List<OneClickFoundReportResponse> reports = reportService.getAllReports();
//        return ResponseEntity.ok(reports);
//    }
//
//}


import lk.chamasha.lost.and.found.controller.request.OneClickFoundReportRequest;
import lk.chamasha.lost.and.found.controller.response.OneClickFoundReportResponse;
import lk.chamasha.lost.and.found.exception.ReportNotFoundException;
import lk.chamasha.lost.and.found.exception.UserNotFoundException;
import lk.chamasha.lost.and.found.service.OneClickFoundReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/found")
@RequiredArgsConstructor
public class OneClickFoundReportController {

    private final OneClickFoundReportService reportService;

    @PostMapping("/report")
    public ResponseEntity<OneClickFoundReportResponse> reportFound(@RequestBody OneClickFoundReportRequest request)
            throws UserNotFoundException {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID is required to report a found item");
        }

        OneClickFoundReportResponse response = reportService.reportFound(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OneClickFoundReportResponse>> getReportsByUser(@PathVariable Long userId)
            throws UserNotFoundException, ReportNotFoundException {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        List<OneClickFoundReportResponse> reports = reportService.getReportsByUser(userId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OneClickFoundReportResponse>> getAllReports() throws ReportNotFoundException {
        List<OneClickFoundReportResponse> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }
}
