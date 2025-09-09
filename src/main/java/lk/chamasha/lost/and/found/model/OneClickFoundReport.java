package lk.chamasha.lost.and.found.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "one_click_found_reports")
public class OneClickFoundReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    private String description;

    private String location;

    private String photoUrl;

    private LocalDateTime reportedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}