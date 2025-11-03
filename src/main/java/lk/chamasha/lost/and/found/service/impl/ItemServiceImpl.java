

package lk.chamasha.lost.and.found.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.chamasha.lost.and.found.controller.request.ItemRequest;
import lk.chamasha.lost.and.found.controller.response.ItemResponse;
import lk.chamasha.lost.and.found.controller.response.UserResponse;
import lk.chamasha.lost.and.found.exception.ItemNotFoundException;
import lk.chamasha.lost.and.found.exception.UserNotFoundException;
import lk.chamasha.lost.and.found.model.Item;
import lk.chamasha.lost.and.found.model.ItemStatus;
import lk.chamasha.lost.and.found.model.User;
import lk.chamasha.lost.and.found.repository.ItemRepository;
import lk.chamasha.lost.and.found.repository.UserRepository;
import lk.chamasha.lost.and.found.service.CloudinaryService;
import lk.chamasha.lost.and.found.service.ItemService;
import lk.chamasha.lost.and.found.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public ItemResponse createItem(String itemJson, MultipartFile image) throws UserNotFoundException {
        ObjectMapper objectMapper = new ObjectMapper();
        ItemRequest request;

        try {
            request = objectMapper.readValue(itemJson, ItemRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JSON data", e);
        }

        // ✅ Validate user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User with ID " + request.getUserId() + " not found"));

        // ✅ Upload image to Cloudinary
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = cloudinaryService.uploadImage(image);
        }

        // ✅ Handle date conversion safely
        LocalDateTime dateTime;
        if (request.getDate() != null) {
            try {
                dateTime = LocalDateTime.parse(request.getDate(), DateTimeFormatter.ISO_DATE_TIME);
            } catch (Exception e) {
                dateTime = LocalDateTime.now();
            }
        } else {
            dateTime = LocalDateTime.now();
        }

        // ✅ Create and save item
        Item item = new Item();
        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setCategory(request.getCategory());
        item.setLocation(request.getLocation());
        item.setDate(dateTime);
        item.setEmergency(request.isEmergency());
        item.setImageUrl(imageUrl);
        item.setStatus(request.getStatus() != null ? request.getStatus() : ItemStatus.LOST);
        item.setPhoneNumber(request.getPhoneNumber()); // ✅ added phone number field
        item.setUser(user);

        Item savedItem = itemRepository.save(item);

        // ✅ Only create alerts if this is a lost item
        if (savedItem.getStatus() == ItemStatus.LOST) {
            notificationService.createNearbyAlerts(savedItem);
        }

        return mapToResponse(item);
    }

    @Override
    public ItemResponse getItemById(Long id) throws ItemNotFoundException {
        return itemRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ItemNotFoundException("Item with ID " + id + " not found"));
    }

    @Override
    public List<ItemResponse> getItemsByUser(Long userId) {
        return itemRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponse> searchItems(String category, String location, boolean emergency) {
        return itemRepository.findAll().stream()
                .filter(i -> (category == null || i.getCategory().equalsIgnoreCase(category)) &&
                        (location == null || i.getLocation().toLowerCase().contains(location.toLowerCase())) &&
                        (!emergency || i.isEmergency()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItem(Long id) throws ItemNotFoundException {
        if (!itemRepository.existsById(id)) {
            throw new ItemNotFoundException("Cannot delete. Item with ID " + id + " not found");
        }
        itemRepository.deleteById(id);
    }

    // ✅ Convert entity → response
    private ItemResponse mapToResponse(Item item) {
        User user = item.getUser();
        return ItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .category(item.getCategory())
                .status(item.getStatus())
                .imageUrl(item.getImageUrl())
                .location(item.getLocation())
                .date(item.getDate())
                .emergency(item.isEmergency())
                .phoneNumber(item.getPhoneNumber()) // ✅ include in response
                .user(UserResponse.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .languagePreference(
                                user.getLanguagePreference() != null
                                        ? user.getLanguagePreference().name()
                                        : "ENGLISH"
                        )
                        .reputationScore(user.getReputationScore())
                        .verified(user.isVerified())
                        .build())
                .build();
    }

    @Override
    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}

