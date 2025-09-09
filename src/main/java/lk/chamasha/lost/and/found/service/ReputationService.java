package lk.chamasha.lost.and.found.service;

import lk.chamasha.lost.and.found.controller.response.ReputationResponse;

public interface ReputationService {
    ReputationResponse getUserReputation(Long userId);
    void incrementReputation(Long userId);
}
