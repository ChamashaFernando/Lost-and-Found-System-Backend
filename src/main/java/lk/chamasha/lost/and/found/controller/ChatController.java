package lk.chamasha.lost.and.found.controller;

import lk.chamasha.lost.and.found.controller.request.ChatMessageRequest;
import lk.chamasha.lost.and.found.controller.request.ChatSessionRequest;
import lk.chamasha.lost.and.found.controller.response.ChatMessageResponse;
import lk.chamasha.lost.and.found.controller.response.ChatSessionResponse;
import lk.chamasha.lost.and.found.exception.ChatSessionNotFoundException;
import lk.chamasha.lost.and.found.exception.UserNotFoundException;
import lk.chamasha.lost.and.found.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // Create a new chat session
    @PostMapping("/session")
    public ResponseEntity<ChatSessionResponse> createSession(@RequestBody ChatSessionRequest request) throws UserNotFoundException {
        ChatSessionResponse response = chatService.createSession(request);
        return ResponseEntity.ok(response);
    }

    // Send a message in a chat session
    @PostMapping("/message")
    public ResponseEntity<ChatMessageResponse> sendMessage(@RequestBody ChatMessageRequest request) throws ChatSessionNotFoundException, UserNotFoundException {
        ChatMessageResponse response = chatService.sendMessage(request);
        return ResponseEntity.ok(response);
    }

    // Get all messages from a specific chat session
    @GetMapping("/session/{sessionId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getMessages(@PathVariable Long sessionId) {
        List<ChatMessageResponse> messages = chatService.getMessages(sessionId);
        return ResponseEntity.ok(messages);
    }
}
