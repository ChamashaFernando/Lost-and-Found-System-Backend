package lk.chamasha.lost.and.found.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatSessionRequest {
    private Long user1Id;
    private Long user2Id;
    private boolean verified;
}
