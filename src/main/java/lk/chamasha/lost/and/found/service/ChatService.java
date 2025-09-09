package lk.chamasha.lost.and.found.service;

import lk.chamasha.lost.and.found.controller.request.ChatMessageRequest;
import lk.chamasha.lost.and.found.controller.request.ChatSessionRequest;
import lk.chamasha.lost.and.found.controller.response.ChatMessageResponse;
import lk.chamasha.lost.and.found.controller.response.ChatSessionResponse;
import lk.chamasha.lost.and.found.exception.ChatSessionNotFoundException;
import lk.chamasha.lost.and.found.exception.UserNotFoundException;

import java.util.List;

public interface ChatService {
    public ChatSessionResponse createSession(ChatSessionRequest request)throws UserNotFoundException;
    public ChatMessageResponse sendMessage(ChatMessageRequest request)throws ChatSessionNotFoundException,UserNotFoundException;
    public List<ChatMessageResponse> getMessages(Long sessionId);
}
