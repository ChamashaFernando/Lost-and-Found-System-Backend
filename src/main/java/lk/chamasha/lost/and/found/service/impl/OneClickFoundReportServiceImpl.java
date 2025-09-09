package lk.chamasha.lost.and.found.service.impl;

import lk.chamasha.lost.and.found.controller.request.OneClickFoundReportRequest;
import lk.chamasha.lost.and.found.controller.response.OneClickFoundReportResponse;
import lk.chamasha.lost.and.found.controller.response.UserResponse;
import lk.chamasha.lost.and.found.exception.ReportNotFoundException;
import lk.chamasha.lost.and.found.exception.UserNotFoundException;
import lk.chamasha.lost.and.found.model.OneClickFoundReport;
import lk.chamasha.lost.and.found.model.User;
import lk.chamasha.lost.and.found.repository.OneClickFoundReportRepository;
import lk.chamasha.lost.and.found.repository.UserRepository;
import lk.chamasha.lost.and.found.service.OneClickFoundReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OneClickFoundReportServiceImpl implements OneClickFoundReportService {

    private final OneClickFoundReportRepository oneClickFoundReportRepository;
    private final UserRepository userRepository;

    @Override
    public OneClickFoundReportResponse reportFound(OneClickFoundReportRequest request) throws UserNotFoundException {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User with ID " + request.getUserId() + " not found"));

        OneClickFoundReport report = OneClickFoundReport.builder()
                .category(request.getCategory())
                .description(request.getDescription())
                .location(request.getLocation())
                .photoUrl(request.getPhotoUrl())
                .reportedAt(LocalDateTime.now())
                .user(user)
                .build();

        OneClickFoundReport savedReport = oneClickFoundReportRepository.save(report);
        return mapToResponse(savedReport);
    }

    @Override
    public List<OneClickFoundReportResponse> getReportsByUser(Long userId) throws UserNotFoundException, ReportNotFoundException {
        // Confirm user exists first
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        List<OneClickFoundReport> reports = oneClickFoundReportRepository.findByUserId(userId);

        if (reports.isEmpty()) {
            throw new ReportNotFoundException("No found reports for user with ID " + userId);
        }

        return reports.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private OneClickFoundReportResponse mapToResponse(OneClickFoundReport report) {
        return OneClickFoundReportResponse.builder()
                .id(report.getId())
                .category(report.getCategory())
                .description(report.getDescription())
                .location(report.getLocation())
                .photoUrl(report.getPhotoUrl())
                .reportedAt(report.getReportedAt())
                .user(mapToUserResponse(report.getUser()))
                .build();
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .languagePreference(user.getLanguagePreference() != null ? user.getLanguagePreference().name() : null)
                .reputationScore(user.getReputationScore())
                .verified(user.isVerified())
                .token(null) // set token if applicable
                .build();
    }
}
