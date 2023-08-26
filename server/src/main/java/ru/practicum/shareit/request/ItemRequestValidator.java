package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemRequestValidator {

    private final ItemRequestRepository itemRequestRepository;
    private final RequestAnswerRepository requestAnswerRepository;


    public void requestSetCreationTime(ItemRequest request) {
        request.setCreated(LocalDateTime.now());
    }

    public List<ItemRequest> formAnswersListForUser(int requesterId) {
        List<ItemRequest> userRequests = itemRequestRepository
                .findAllByRequesterIdOrderByCreatedDesc(requesterId);

        for (ItemRequest i : userRequests) {
            int requestId = i.getId();
            List<RequestAnswer> answersForRequest = requestAnswerRepository
                    .findAllByRequestId(requestId);
            i.setItems(answersForRequest);
        }
        return userRequests;
    }

    public ItemRequest getInformationAboutSingleRequests(int requestId) {
        ItemRequest request = itemRequestRepository
                .findById(requestId)
                .orElseThrow(() -> new NotFoundException("Не найдена заявка с id: " + requestId));
        List<RequestAnswer> answersForRequest = requestAnswerRepository
                .findAllByRequestId(requestId);
        request.setItems(answersForRequest);

        return request;
    }

    public List<ItemRequest> getOtherUsersRequestsPaginated(int requesterId, Integer startPage, Integer outputSize) {

        PageRequest pageRequest = PageRequest.of(startPage, outputSize);

        List<ItemRequest> userRequests = itemRequestRepository
                .findAllByRequesterIdIsNotOrderByCreatedDesc(requesterId, pageRequest);

        for (ItemRequest i : userRequests) {
            int requestId = i.getId();
            List<RequestAnswer> answersForRequest = requestAnswerRepository
                    .findAllByRequestId(requestId);
            i.setItems(answersForRequest);
        }
        return userRequests;
    }
}