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
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private String category;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    private String photoUrl;

    private String location;

    private LocalDateTime date;

    private boolean emergency;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}