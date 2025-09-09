package lk.chamasha.lost.and.found.service;

import lk.chamasha.lost.and.found.controller.request.ClaimRequestRequest;
import lk.chamasha.lost.and.found.controller.response.ClaimRequestResponse;
import lk.chamasha.lost.and.found.exception.ClaimRequestNotFoundException;
import lk.chamasha.lost.and.found.exception.ItemNotFoundException;
import lk.chamasha.lost.and.found.exception.UserNotFoundException;

import java.util.List;

public interface ClaimRequestService {
    public ClaimRequestResponse createClaim(ClaimRequestRequest request)throws UserNotFoundException, ItemNotFoundException;
    public List<ClaimRequestResponse> getClaimsByItem(Long itemId);
    public void approveClaim(Long claimId)throws ClaimRequestNotFoundException;
}
