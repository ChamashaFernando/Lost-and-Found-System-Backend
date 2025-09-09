package lk.chamasha.lost.and.found.service.impl;

import lk.chamasha.lost.and.found.controller.request.ChatMessageRequest;
import lk.chamasha.lost.and.found.controller.request.ChatSessionRequest;
import lk.chamasha.lost.and.found.controller.response.ChatMessageResponse;
import lk.chamasha.lost.and.found.controller.response.ChatSessionResponse;
import lk.chamasha.lost.and.found.controller.response.UserResponse;
import lk.chamasha.lost.and.found.exception.ChatSessionNotFoundException;
import lk.chamasha.lost.and.found.exception.UserNotFoundException;
import lk.chamasha.lost.and.found.model.ChatMessage;
import lk.chamasha.lost.and.found.model.ChatSession;
import lk.chamasha.lost.and.found.model.User;
import lk.chamasha.lost.and.found.repository.ChatMessageRepository;
import lk.chamasha.lost.and.found.repository.ChatSessionRepository;
import lk.chamasha.lost.and.found.repository.UserRepository;
import lk.chamasha.lost.and.found.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Override
    public ChatSessionResponse createSession(ChatSessionRequest request)throws UserNotFoundException{
        User user1 = userRepository.findById(request.getUser1Id())
                .orElseThrow(() -> new UserNotFoundException("User1 with ID " + request.getUser1Id() + " not found"));
        User user2 = userRepository.findById(request.getUser2Id())
                .orElseThrow(() -> new UserNotFoundException("User2 with ID " + request.getUser2Id() + " not found"));

        ChatSession session = new ChatSession();
        session.setUser1(user1);
        session.setUser2(user2);
        session.setStartedAt(LocalDateTime.now());
        session.setVerified(request.isVerified());

        return mapToSessionResponse(chatSessionRepository.save(session));
    }

    @Override
    public ChatMessageResponse sendMessage(ChatMessageRequest request)throws ChatSessionNotFoundException,UserNotFoundException {
        ChatSession session = chatSessionRepository.findById(request.getChatSessionId())
                .orElseThrow(() -> new ChatSessionNotFoundException("Chat session with ID " + request.getChatSessionId() + " not found"));

        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new UserNotFoundException("Sender with ID " + request.getSenderId() + " not found"));

        // Optional validation: check if sender is part of the chat session
        if (!session.getUser1().getId().equals(sender.getId()) &&
                !session.getUser2().getId().equals(sender.getId())) {
            throw new UserNotFoundException("Sender is not a participant in this chat session");
        }

        ChatMessage message = new ChatMessage();
        message.setChatSession(session);
        message.setSender(sender);
        message.setContent(request.getContent());
        message.setSentAt(LocalDateTime.now());

        return mapToMessageResponse(chatMessageRepository.save(message));
    }

    @Override
    public List<ChatMessageResponse> getMessages(Long sessionId) {
        return chatMessageRepository.findByChatSession_Id(sessionId).stream()
                .map(this::mapToMessageResponse)
                .collect(Collectors.toList());
    }

    private ChatSessionResponse mapToSessionResponse(ChatSession session) {
        return ChatSessionResponse.builder()
                .id(session.getId())
                .verified(session.isVerified())
                .user1(mapToUserResponse(session.getUser1()))
                .user2(mapToUserResponse(session.getUser2()))
                .build();
    }

    private ChatMessageResponse mapToMessageResponse(ChatMessage message) {
        return ChatMessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .sentAt(message.getSentAt())
                .sender(mapToUserResponse(message.getSender()))
                .build();
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .languagePreference(user.getLanguagePreference().name())
                .reputationScore(user.getReputationScore())
                .verified(user.isVerified())
                .build();
    }
}
