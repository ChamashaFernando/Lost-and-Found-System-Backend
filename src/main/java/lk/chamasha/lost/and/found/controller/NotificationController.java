package lk.chamasha.lost.and.found.controller;

import lk.chamasha.lost.and.found.controller.response.NotificationResponse;
import lk.chamasha.lost.and.found.model.Item;
import lk.chamasha.lost.and.found.model.Notification;
import lk.chamasha.lost.and.found.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lk.chamasha.lost.and.found.controller.response.ItemResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getUserNotifications(userId);

        List<NotificationResponse> response = notifications.stream().map(n -> {
            Item item = n.getItem(); // lazy load issue
            ItemResponse itemResponse = null;

            if (item != null) {
                itemResponse = ItemResponse.builder()
                        .id(item.getId())
                        .title(item.getTitle())
                        .description(item.getDescription())
                        .category(item.getCategory())
                        .location(item.getLocation())
                        .status(item.getStatus())
                        .emergency(item.isEmergency())
                        .phoneNumber(item.getPhoneNumber())
                        .imageUrl(item.getImageUrl())
                        .date(item.getDate())
                        .build();
            }

            return NotificationResponse.builder()
                    .id(n.getId())
                    .title(n.getTitle())
                    .message(n.getMessage())
                    .seen(n.isSeen())
                    .createdAt(n.getCreatedAt())
                    .item(itemResponse)
                    .build();
        }).toList();


        return ResponseEntity.ok(response);
    }


    // Optional: mark a notification as seen
    @PostMapping("/{notificationId}/seen")
    public void markAsSeen(@PathVariable Long notificationId) {
        notificationService.markNotificationAsSeen(notificationId);
    }
}
