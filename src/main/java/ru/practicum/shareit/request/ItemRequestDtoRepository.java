
package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestDtoRepository extends JpaRepository<ItemRequestDto, Integer> {

    List<ItemRequestDto> findAllByRequesterIdOrderByCreatedDesc(int requesterId);

}


