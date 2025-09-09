package lk.chamasha.lost.and.found.service;


import lk.chamasha.lost.and.found.controller.request.ItemRequest;
import lk.chamasha.lost.and.found.controller.response.ItemResponse;
import lk.chamasha.lost.and.found.exception.ItemNotFoundException;
import lk.chamasha.lost.and.found.exception.UserNotFoundException;

import java.util.List;

public interface ItemService {

    public ItemResponse createItem(ItemRequest request)throws UserNotFoundException;
    public ItemResponse getItemById(Long id)throws ItemNotFoundException;
    public List<ItemResponse> getItemsByUser(Long userId);
    public List<ItemResponse> searchItems(String category, String location, boolean emergency);
    public void deleteItem(Long id)throws ItemNotFoundException;



}
