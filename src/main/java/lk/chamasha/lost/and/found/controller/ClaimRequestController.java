package lk.chamasha.lost.and.found.controller;

import jakarta.annotation.security.RolesAllowed;
import lk.chamasha.lost.and.found.controller.request.ClaimRequestRequest;
import lk.chamasha.lost.and.found.controller.response.ClaimRequestResponse;
import lk.chamasha.lost.and.found.exception.ClaimRequestNotFoundException;
import lk.chamasha.lost.and.found.exception.ItemNotFoundException;
import lk.chamasha.lost.and.found.exception.UserNotFoundException;
import lk.chamasha.lost.and.found.service.ClaimRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
public class ClaimRequestController {

    private final ClaimRequestService claimRequestService;


//    @RolesAllowed({"ADMIN","STUDENT"})
    @PostMapping
    public ResponseEntity<ClaimRequestResponse> createClaim(@RequestBody ClaimRequestRequest request)
            throws UserNotFoundException, ItemNotFoundException {
        ClaimRequestResponse response = claimRequestService.createClaim(request);
        return ResponseEntity.ok(response);
    }

    // Get all claim requests for a specific item
    @RolesAllowed({"ADMIN","STUDENT"})
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<ClaimRequestResponse>> getClaimsByItem(@PathVariable Long itemId) {
        List<ClaimRequestResponse> claims = claimRequestService.getClaimsByItem(itemId);
        return ResponseEntity.ok(claims);
    }

    // Approve a specific claim
    @RolesAllowed({"ADMIN","STUDENT"})
    @PutMapping("/{claimId}/approve")
    public ResponseEntity<Void> approveClaim(@PathVariable Long claimId)
            throws ClaimRequestNotFoundException {
        claimRequestService.approveClaim(claimId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Token works!");
    }


}
