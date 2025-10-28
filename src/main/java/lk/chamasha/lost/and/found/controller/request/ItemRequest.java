package lk.chamasha.lost.and.found.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String location;
    private String date;
    private boolean emergency;
    private Long userId;
    private String phoneNumber;
}
