package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.UserValidator;

import java.util.List;

@Slf4j
@Getter
@Setter
@Service
@RequiredArgsConstructor
@Transactional
public class RequestService {

    private final UserValidator userValidator;
    private final ItemRequestValidator requestValidator;
    private final ItemRequestRepository requestRepository;

    @Transactional
    public ItemRequest addRequest(int requesterId, ItemRequest request) {
        log.info("Добавление запроса вещи от пользователя с id: " + requesterId);
        userValidator.checkUserExists(requesterId);
        request.setRequesterId(requesterId);
        requestValidator.requestValidateAndSetCreationTime(request);

        return requestRepository.save(request);
    }

    public List<ItemRequestDto> getRequestsForUser(int requesterId) {
        log.info("Просмотр заявок на вещи от пользователя с id: " + requesterId);
        userValidator.checkUserExists(requesterId);

        return requestValidator.formAnswersListForUser(requesterId);
    }

    public List<ItemRequest> getOtherUsersRequests(Integer requesterId, Integer startPage, Integer outputSize) {
        userValidator.checkUserExists(requesterId);
        log.info("Просмотр заявок всех кроме пользователя с id: " + requesterId);

        return requestValidator.getOtherUsersRequestsPaginated(requesterId, startPage, outputSize);
    }

    public ItemRequestDto getInformationAboutSingleRequests(int requesterId, int requestId) {
        log.info("Просмотр заявки с id: " + requestId);
        userValidator.checkUserExists(requesterId);
        return requestValidator.getInformationAboutSingleRequests(requestId);
    }
}