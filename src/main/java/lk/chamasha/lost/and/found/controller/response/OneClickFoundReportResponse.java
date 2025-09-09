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
public class OneClickFoundReportResponse {
    private Long id;
    private String category;
    private String description;
    private String location;
    private String photoUrl;
    private LocalDateTime reportedAt;
    private UserResponse user;
}