package lk.chamasha.lost.and.found.controller.response;

import lk.chamasha.lost.and.found.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private UserRole role;
    private String languagePreference;
    private int reputationScore;
    private boolean verified;
    private String token;
}