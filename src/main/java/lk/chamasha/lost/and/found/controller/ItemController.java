package lk.chamasha.lost.and.found.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.security.RolesAllowed;
import lk.chamasha.lost.and.found.controller.request.ItemRequest;
import lk.chamasha.lost.and.found.controller.response.ItemResponse;
import lk.chamasha.lost.and.found.exception.ItemNotFoundException;
import lk.chamasha.lost.and.found.exception.UserNotFoundException;
import lk.chamasha.lost.and.found.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 🟢 Multipart support
    @RolesAllowed({"ADMIN","STUDENT"})
    @PostMapping(value = "/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ItemResponse> addItem(
            @RequestPart("item") String itemJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws JsonProcessingException,UserNotFoundException{

        ObjectMapper objectMapper = new ObjectMapper();
        ItemRequest itemRequest = objectMapper.readValue(itemJson, ItemRequest.class);

        ItemResponse response = itemService.createItem(itemJson, image);
        return ResponseEntity.ok(response);
    }

    @RolesAllowed({"ADMIN","STUDENT"})
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long id) throws ItemNotFoundException {
        ItemResponse item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @RolesAllowed({"ADMIN","STUDENT"})
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ItemResponse>> getItemsByUser(@PathVariable Long userId) {
        List<ItemResponse> items = itemService.getItemsByUser(userId);
        return ResponseEntity.ok(items);
    }

    @RolesAllowed({"ADMIN","STUDENT"})
    @GetMapping("/search")
    public ResponseEntity<List<ItemResponse>> searchItems(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "false") boolean emergency
    ) {
        List<ItemResponse> results = itemService.searchItems(category, location, emergency);
        return ResponseEntity.ok(results);
    }

    @RolesAllowed({"ADMIN","STUDENT"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) throws ItemNotFoundException {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    // ItemController.java
    @RolesAllowed({"ADMIN","STUDENT"})
    @GetMapping
    public List<ItemResponse> getAllItems() {
        return itemService.getAllItems();
    }

}
