package lk.chamasha.lost.and.found.controller.response;

import lk.chamasha.lost.and.found.model.ClaimStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimRequestResponse {
    private Long id;
    private String message;
    private ClaimStatus status;
    private LocalDateTime createdAt;
    private UserResponse user;
    private ItemResponse item;
}