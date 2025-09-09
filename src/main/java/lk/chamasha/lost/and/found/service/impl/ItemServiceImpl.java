package lk.chamasha.lost.and.found.service.impl;

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
import lk.chamasha.lost.and.found.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemResponse createItem(ItemRequest request)throws UserNotFoundException {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User with ID " + request.getUserId() + " not found"));

        Item item = new Item();
        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setCategory(request.getCategory());
        item.setLocation(request.getLocation());
        item.setDate(request.getDate());
        item.setEmergency(request.isEmergency());
        item.setPhotoUrl(request.getPhotoUrl());
        item.setStatus(request.getStatus() != null ? request.getStatus() : ItemStatus.LOST);
        item.setUser(user);

        return mapToResponse(itemRepository.save(item));
    }

    @Override
    public ItemResponse getItemById(Long id)throws ItemNotFoundException {
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
    public void deleteItem(Long id)throws ItemNotFoundException {
        if (!itemRepository.existsById(id)) {
            throw new ItemNotFoundException("Cannot delete. Item with ID " + id + " not found");
        }
        itemRepository.deleteById(id);
    }

    private ItemResponse mapToResponse(Item item) {
        User user = item.getUser();
        return ItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .category(item.getCategory())
                .status(item.getStatus())
                .photoUrl(item.getPhotoUrl())
                .location(item.getLocation())
                .date(item.getDate())
                .emergency(item.isEmergency())
                .user(UserResponse.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .languagePreference(user.getLanguagePreference().name())
                        .reputationScore(user.getReputationScore())
                        .verified(user.isVerified())
                        .build())
                .build();
    }
}
