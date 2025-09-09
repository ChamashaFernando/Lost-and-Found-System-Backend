package lk.chamasha.lost.and.found.service.impl;

import lk.chamasha.lost.and.found.controller.response.NotificationResponse;
import lk.chamasha.lost.and.found.controller.response.UserResponse;
import lk.chamasha.lost.and.found.exception.NotificationNotFoundException;
import lk.chamasha.lost.and.found.model.Notification;
import lk.chamasha.lost.and.found.model.User;
import lk.chamasha.lost.and.found.repository.NotificationRepository;
import lk.chamasha.lost.and.found.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationResponse> getUserNotifications(Long userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long id)throws NotificationNotFoundException {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .message(notification.getMessage())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .user(mapToUserResponse(notification.getUser()))
                .build();
    }

    private UserResponse mapToUserResponse(User user) {
        if (user == null) return null;
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail()) // ✅ or user.getUsername()
                .role(user.getRole())
                .languagePreference(user.getLanguagePreference().name())
                .reputationScore(user.getReputationScore())
                .verified(user.isVerified())
                .build();
    }
}
