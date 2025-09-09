package lk.chamasha.lost.and.found.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReputationResponse {
    private Long id;
    private int scoreChange;
    private String reason;
    private LocalDateTime updatedAt;
    private UserResponse user;
}