package lk.chamasha.lost.and.found.controller;

import lk.chamasha.lost.and.found.controller.response.NotificationResponse;
import lk.chamasha.lost.and.found.exception.NotificationNotFoundException;
import lk.chamasha.lost.and.found.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // Get all notifications for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(@PathVariable Long userId) {
        List<NotificationResponse> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    // Mark a notification as read
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) throws NotificationNotFoundException {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }
}
