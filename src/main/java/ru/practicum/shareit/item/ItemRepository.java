package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByOwnerId(int ownerId);

    //  List<Item> findByNameOrDescriptionContainingIgnoreCase(String searchName,String searchDescription);
    Optional<Item> getByIdAndOwnerId(int id, int ownerId);

    List<Item> findByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(String searchName, String searchDescription); //магия JPA
}
