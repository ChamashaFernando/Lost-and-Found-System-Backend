package lk.chamasha.lost.and.found.service.impl;

import lk.chamasha.lost.and.found.controller.response.ReputationResponse;
import lk.chamasha.lost.and.found.controller.response.UserResponse;
import lk.chamasha.lost.and.found.model.Reputation;
import lk.chamasha.lost.and.found.model.User;
import lk.chamasha.lost.and.found.repository.ReputationRepository;
import lk.chamasha.lost.and.found.repository.UserRepository;
import lk.chamasha.lost.and.found.service.ReputationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReputationServiceImpl implements ReputationService {

    private final ReputationRepository reputationRepository;
    private final UserRepository userRepository;

    @Override
    public ReputationResponse getUserReputation(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reputation latestReputation = reputationRepository
                .findTopByUserOrderByUpdatedAtDesc(user)
                .orElse(null);

        if (latestReputation == null) {
            return ReputationResponse.builder()
                    .id(null)
                    .scoreChange(0)
                    .reason("No reputation history")
                    .updatedAt(null)
                    .user(mapToUserResponse(user))
                    .build();
        }

        return mapToResponse(latestReputation);
    }

    @Override
    public void incrementReputation(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reputation reputation = Reputation.builder()
                .user(user)
                .scoreChange(1)
                .reason("Positive action") // Optional: customize as needed
                .updatedAt(LocalDateTime.now())
                .build();

        reputationRepository.save(reputation);
    }

    private ReputationResponse mapToResponse(Reputation reputation) {
        return ReputationResponse.builder()
                .id(reputation.getId())
                .scoreChange(reputation.getScoreChange())
                .reason(reputation.getReason())
                .updatedAt(reputation.getUpdatedAt())
                .user(mapToUserResponse(reputation.getUser()))
                .build();
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail()) // ✅ or user.getUsername()
                .role(user.getRole())
                .languagePreference(user.getLanguagePreference().name())
                .reputationScore(user.getReputationScore())
                .verified(user.isVerified())
//                .token(token) // if you're setting a JWT or similar
                .build();
    }
}
