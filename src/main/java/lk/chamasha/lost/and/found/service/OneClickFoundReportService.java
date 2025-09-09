package lk.chamasha.lost.and.found.service;

import lk.chamasha.lost.and.found.controller.request.OneClickFoundReportRequest;
import lk.chamasha.lost.and.found.controller.response.OneClickFoundReportResponse;
import lk.chamasha.lost.and.found.exception.ReportNotFoundException;
import lk.chamasha.lost.and.found.exception.UserNotFoundException;

import java.util.List;

public interface OneClickFoundReportService {
    public OneClickFoundReportResponse reportFound(OneClickFoundReportRequest request) throws UserNotFoundException;
    public List<OneClickFoundReportResponse> getReportsByUser(Long userId) throws UserNotFoundException, ReportNotFoundException;
}
