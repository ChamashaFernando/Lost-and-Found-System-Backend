package lk.chamasha.lost.and.found.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OneClickFoundReportRequest {
    private String category;
    private String description;
    private String location;
    private String photoUrl;
    private Long userId;
}