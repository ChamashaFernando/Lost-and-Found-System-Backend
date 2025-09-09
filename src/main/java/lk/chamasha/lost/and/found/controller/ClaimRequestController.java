package lk.chamasha.lost.and.found.controller;

import lk.chamasha.lost.and.found.controller.request.ClaimRequestRequest;
import lk.chamasha.lost.and.found.controller.response.ClaimRequestResponse;
import lk.chamasha.lost.and.found.exception.ClaimRequestNotFoundException;
import lk.chamasha.lost.and.found.exception.ItemNotFoundException;
import lk.chamasha.lost.and.found.exception.UserNotFoundException;
import lk.chamasha.lost.and.found.service.ClaimRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
public class ClaimRequestController {

    private final ClaimRequestService claimRequestService;

    // Submit a new claim request
    @PostMapping
    public ResponseEntity<ClaimRequestResponse> createClaim(@RequestBody ClaimRequestRequest request)
            throws UserNotFoundException, ItemNotFoundException {
        ClaimRequestResponse response = claimRequestService.createClaim(request);
        return ResponseEntity.ok(response);
    }

    // Get all claim requests for a specific item
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<ClaimRequestResponse>> getClaimsByItem(@PathVariable Long itemId) {
        List<ClaimRequestResponse> claims = claimRequestService.getClaimsByItem(itemId);
        return ResponseEntity.ok(claims);
    }

    // Approve a specific claim
    @PutMapping("/{claimId}/approve")
    public ResponseEntity<Void> approveClaim(@PathVariable Long claimId)
            throws ClaimRequestNotFoundException {
        claimRequestService.approveClaim(claimId);
        return ResponseEntity.noContent().build();
    }
}
