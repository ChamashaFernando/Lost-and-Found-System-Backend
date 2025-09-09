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
public class ChatSessionResponse {
    private Long id;
    private boolean verified;
    private LocalDateTime startedAt;
    private UserResponse user1;
    private UserResponse user2;
}
