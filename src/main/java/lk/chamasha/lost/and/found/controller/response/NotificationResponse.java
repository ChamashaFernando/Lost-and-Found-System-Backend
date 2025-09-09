package lk.chamasha.lost.and.found.controller.response;

import lk.chamasha.lost.and.found.controller.response.UserResponse;

import lk.chamasha.lost.and.found.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private Long id;
    private NotificationType type;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;
    private UserResponse user;
}
