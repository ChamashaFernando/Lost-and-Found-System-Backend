package lk.chamasha.lost.and.found.controller.request;

import lk.chamasha.lost.and.found.model.ClaimStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimRequestRequest {
    private String message;
    private ClaimStatus status;
    private Long userId;
    private Long itemId;
}
