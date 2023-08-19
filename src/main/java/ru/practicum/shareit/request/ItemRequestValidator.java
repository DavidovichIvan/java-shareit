package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.DataBaseException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemRequestValidator {

    private final ItemRepository itemRepository;
    private final ItemRequestDtoRepository itemRequestDtoRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final RequestAnswerRepository requestAnswerRepository;

    public void requestValidateAndSetCreationTime(ItemRequest request) {
        String text = request.getDescription();
        if (text == null || text.isBlank()) {
            throw new DataBaseException("Введен пустой комментарий, id пользователя: " + request.getRequesterId());
        }
        request.setCreated(LocalDateTime.now());
    }

   // GET /requests — получить список своих запросов вместе с данными об ответах на них. //то есть список возвращается запросов одного конкретного пользователя
    // Для каждого запроса должны указываться описание, дата и время создания и список ответов //то есть внутри каждой заявки еще список вещей, которые в ответ на заявку созданы были (список внутри списка)
    // в формате: id вещи, название, её описание description, а также requestId запроса и признак доступности вещи available.
    // Запросы должны возвращаться в отсортированном порядке от более новых к более старым.

    public List<ItemRequestDto> formAnswersListForUser(int requesterId) {
        List<ItemRequestDto> userRequests = itemRequestDtoRepository.findAllByRequesterIdOrderByCreatedDesc(requesterId); //сформировали список запросов(заявок на вещи) конкретного пользователя, сразу отсортировали по времени создания

        for (ItemRequestDto i : userRequests) { //прошлись по списку
            int requestId = i.getId();
            List<RequestAnswer> answersForRequest = requestAnswerRepository.findAllByRequestId(requestId); //в каждый запрос нашли все ответы (из таблицы items по полю requestId)
            i.setItems(answersForRequest); //дополнили информацию о запросе пользователя ответами
           }
        return userRequests;
    }

    public ItemRequestDto getInformationAboutSingleRequests(int requestId){
        ItemRequestDto request = itemRequestDtoRepository
                .findById(requestId)
                .orElseThrow(()->new NotFoundException("Не найдена заявка с id: " + requestId));
        List<RequestAnswer> answersForRequest = requestAnswerRepository.findAllByRequestId(requestId);
      request.setItems(answersForRequest);

      return request;
    }

public  List<ItemRequest> getOtherUsersRequestsPaginated(int requesterId, Integer startPage, Integer outputSize) {
if (outputSize == null || outputSize <= 0) {
    throw new DataBaseException("Количество объектов, подлежащих выводу на одной странице, должно быть положительным.");
}
    if (startPage < 0) {
        throw new DataBaseException("Номер начальной страницы не может быть отрицательным.");
    }

    //тут логичнее было бы выставить параметр по умолчанию для такого случая вместо исключения, но есть автотест который ждет ошибку если size = 0 или -1.
// Вообще из раза в раз вижу что некоторые тесты постмана фактически расширяют ТЗ. На мой взгляд это неправильно в принципе.
// Так как приходится иной раз рабочую реализацию подгонять.
// Хотя те кто пишет эти тесты железные люди сотнями шпарят их однотипные).

    PageRequest pageRequest = PageRequest.of(startPage, outputSize);
    //таким макаром указывается возвращаемый номер страницы и количество элементов отображаемых на странице. Для букинга сделал как в ТЗ - не со страницы а с порядкового номера объекта в списке.
    // Что логично в принципе. Эти параметры взаимосвязаны,
    // то есть если запрос возвращает 12 элементов, то количество страниц зависит от того сколько мы хотм чтобы вывод осуществлял:
    // по 3 элемента значит будет 4 страницы по 2 элемента - 6 страниц.

    List<ItemRequest> userRequests = itemRequestRepository.findAllByRequesterIdIsNotOrderByCreatedDesc(requesterId, pageRequest);

    for (ItemRequest i : userRequests) {
        int requestId = i.getId();
        List<RequestAnswer> answersForRequest = requestAnswerRepository.findAllByRequestId(requestId);
        i.setItems(answersForRequest);
    }
    return userRequests;
   // return itemRequestRepository.findAllByRequesterIdIsNotOrderByCreatedDesc(requesterId, pageRequest); //так пагинация работает но нужно еще ответы прикрутить
    //      return requestRepository.findAllByRequesterIdIsNotOrderByCreatedDesc(requesterId) //это без пагинации
}

}
