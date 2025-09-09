package lk.chamasha.lost.and.found.service.impl;

import lk.chamasha.lost.and.found.controller.request.ClaimRequestRequest;
import lk.chamasha.lost.and.found.controller.response.ClaimRequestResponse;
import lk.chamasha.lost.and.found.controller.response.ItemResponse;
import lk.chamasha.lost.and.found.controller.response.UserResponse;
import lk.chamasha.lost.and.found.exception.ClaimRequestNotFoundException;
import lk.chamasha.lost.and.found.exception.ItemNotFoundException;
import lk.chamasha.lost.and.found.exception.UserNotFoundException;
import lk.chamasha.lost.and.found.model.ClaimRequest;
import lk.chamasha.lost.and.found.model.ClaimStatus;
import lk.chamasha.lost.and.found.model.Item;
import lk.chamasha.lost.and.found.model.User;
import lk.chamasha.lost.and.found.repository.ClaimRequestRepository;
import lk.chamasha.lost.and.found.repository.ItemRepository;
import lk.chamasha.lost.and.found.repository.UserRepository;
import lk.chamasha.lost.and.found.service.ClaimRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClaimRequestServiceImpl implements ClaimRequestService {

    private final ClaimRequestRepository claimRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ClaimRequestResponse createClaim(ClaimRequestRequest request)throws UserNotFoundException,ItemNotFoundException {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + request.getItemId()));

        ClaimRequest claim = new ClaimRequest();
        claim.setUser(user);
        claim.setItem(item);
        claim.setStatus(ClaimStatus.PENDING);
        claim.setMessage(request.getMessage());
        claim.setCreatedAt(LocalDateTime.now());

        return mapToResponse(claimRequestRepository.save(claim));
    }

    @Override
    public List<ClaimRequestResponse> getClaimsByItem(Long itemId) {
        return claimRequestRepository.findByItemId(itemId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void approveClaim(Long claimId)throws ClaimRequestNotFoundException {
        ClaimRequest claim = claimRequestRepository.findById(claimId)
                .orElseThrow(() -> new ClaimRequestNotFoundException("Claim not found with id: " + claimId));
        claim.setStatus(ClaimStatus.APPROVED);
        claimRequestRepository.save(claim);
    }

    private ClaimRequestResponse mapToResponse(ClaimRequest claim) {
        return ClaimRequestResponse.builder()
                .id(claim.getId())
                .message(claim.getMessage())
                .status(claim.getStatus())
                .createdAt(claim.getCreatedAt())
                .user(UserResponse.builder()
                        .id(claim.getUser().getId())
                        .fullName(claim.getUser().getFullName())
                        .email(claim.getUser().getEmail())
                        .reputationScore(claim.getUser().getReputationScore())
                        .verified(claim.getUser().isVerified())
                        .build())
                .item(ItemResponse.builder()
                        .id(claim.getItem().getId())
                        .title(claim.getItem().getTitle())
                        .description(claim.getItem().getDescription())
                        .category(claim.getItem().getCategory())
                        .status(claim.getItem().getStatus())
                        .photoUrl(claim.getItem().getPhotoUrl())
                        .location(claim.getItem().getLocation())
                        .date(claim.getItem().getDate())
                        .emergency(claim.getItem().isEmergency())
                        .user(UserResponse.builder()
                                .id(claim.getItem().getUser().getId())
                                .fullName(claim.getItem().getUser().getFullName())
                                .email(claim.getItem().getUser().getEmail())
                                .reputationScore(claim.getItem().getUser().getReputationScore())
                                .verified(claim.getItem().getUser().isVerified())
                                .build())
                        .build())
                .build();
    }

}
