package lk.chamasha.lost.and.found.controller.request;

import lk.chamasha.lost.and.found.model.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequest {
    private String title;
    private String description;
    private String category;
    private ItemStatus status;
    private String photoUrl;
    private String location;
    private LocalDateTime date;
    private boolean emergency;
    private Long userId;
}