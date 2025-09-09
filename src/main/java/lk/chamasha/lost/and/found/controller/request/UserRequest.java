package lk.chamasha.lost.and.found.controller.request;

import lk.chamasha.lost.and.found.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private String fullName;
    private String email;
    private String password;
    private UserRole role;
    private String languagePreference;
    private Double latitude;
    private Double longitude;

}
