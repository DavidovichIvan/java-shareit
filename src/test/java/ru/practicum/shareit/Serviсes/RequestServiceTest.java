package ru.practicum.shareit.Serviсes;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestDto;
import ru.practicum.shareit.request.RequestService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Data
public class RequestServiceTest {
    private final RequestService requestService;
    private final int requesterId = 1;

    private List<ItemRequest> requestList = new ArrayList<>();

    private ItemRequest testRequest = new ItemRequest("Морской бинокль");

    @AfterEach
    public void clean() {
        requestList.clear();
    }

    @Test
    public void addRequestAndGetAllRequestsAndCheckDescriptionIsNotEmpty() {
        requestService.addRequest(requesterId, testRequest);

        requestList = requestService.getRequestRepository().findAll();
        Assertions.assertThat(requestList.contains(testRequest));
        Assertions.assertThat(requestList
                .get(requestList.size() - 1)
                .getDescription()
                .equalsIgnoreCase("Морской бинокль"));

        for (ItemRequest r : requestList) {
            assertThat(!r.getDescription().isEmpty());
        }
    }

    @Test
    public void getOtherUsersRequests() {
        int from = 0;
        int size = 20;
        requestService.addRequest(requesterId, testRequest);
        requestList = requestService.getOtherUsersRequests(requesterId, from, size);
        assertThat(!requestList.isEmpty());
        for (ItemRequest r : requestList) {
            assertThat(r.getRequesterId() != requesterId);

        }
    }

    @Test
    public void getRequestsForUser() {
        List<ItemRequestDto> requestList = requestService.getRequestsForUser(requesterId);
        assertThat(!requestList.isEmpty());
        for (ItemRequestDto i : requestList) {
            assertThat(i.getRequesterId() == requesterId);
        }
    }

    @Test
    public void getInformationAboutSingleRequests() {
        testRequest.setRequesterId(requesterId);
        ItemRequest item = requestService.addRequest(requesterId, testRequest);
        int requestId = item.getId();

        ItemRequestDto itemDto = requestService.getInformationAboutSingleRequests(requesterId, requestId);

        assertThat(itemDto.getRequesterId() == requesterId);
        assertThat(itemDto.getDescription().equalsIgnoreCase("Морской бинокль"));

    }
}