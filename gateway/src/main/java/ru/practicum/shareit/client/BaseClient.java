package ru.practicum.shareit.client;

import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class BaseClient {//шаблон, который будет корректироваться наследниками для каждого контроллера

    protected final RestTemplate rest; //RestTemplate это специальный клиент в Spring
    // для отправки http-запросов. Он предоставляет удобные API для легкого вызова конечных точек REST’а в одну строку.
    //тут мы будем его заполнять из наследников; передавая свой эндпоинт
    // и адрес сервера нашего для обращения = то есть по сути формируем новый запрос на сервер от gateway контроллера

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    } //конструктор в который мы передаем данные из каждого объекта-клиента (из наследника); точнее не так, этот конструктор наследниками используется для себя

    protected ResponseEntity<Object> get(String path) {
        return get(path, null, null);
    }

    protected ResponseEntity<Object> get(String path, long userId) {
        return get(path, userId, null);
    }

    protected ResponseEntity<Object> get(String path, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, null, parameters, null);
    }

    protected ResponseEntity<Object> get(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, userId, parameters, null);
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return post(path, null, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, long userId, T body) { //методы закциклены на такие же но с другой сигнатурой пирамидкой;
        //при этом делает и возвращает запрос только один нижний метод, до этого идет просто переадресация с указанием что какой то параметр = null
        //Типа хотим чтобы родительский класс универсальность методов обеспечивал
        return post(path, userId, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, Long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, userId, parameters, body);
    }

    protected <T> ResponseEntity<Object> put(String path, long userId, T body) {
        return put(path, userId, null, body);
    }

    protected <T> ResponseEntity<Object> put(String path, long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PUT, path, userId, parameters, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, T body) {
        return patch(path, null, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, long userId) {
        return patch(path, userId, null, null);
    }

    protected <T> ResponseEntity<Object> patch(String path, long userId, T body) {
        return patch(path, userId, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, long userId, @Nullable Map<String, Object> parameters) {
        return patch(path, userId, parameters, null);
    }

    protected <T> ResponseEntity<Object> patch(String path, Long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PATCH, path, userId, parameters, body);
    }

    protected ResponseEntity<Object> delete(String path) {
        return delete(path, null, null);
    }

    protected ResponseEntity<Object> delete(String path, long userId) {
        return delete(path, userId, null);
    }

    protected ResponseEntity<Object> delete(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.DELETE, path, userId, parameters, null);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, Long userId, @Nullable Map<String, Object> parameters, @Nullable T body) { //вот тут мы передаем все нужные параметры которые считали из запроса
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders(userId)); //записали тело запроса и несколько настроек (методом ниже) - по сути завернули запрос в объект

        ResponseEntity<Object> shareitServerResponse;  //это сложный объект который содержит все данные о запросе (то есть объект-запрос)
        try {
            if (parameters != null) {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters); //вот по сути отсюда идет обращение дальше на сервер для всех эндпоинтов;
                // в частности в объекте rest зашита строка запроса к серверной части, то есть мы собственно тут запрос и генерируем, а сервер отрабатывает и возвращает что-то
            } else { //тут разница только в том были параметры в контроллер переданы или нет

                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class); //и отсюда; в объект rest уже зашит основной путь запроса
                // (через конструктор при создании дочернего объекта который имеет свой эндпоинт).
                // Дальше разбиравем: строка path - это дополнительный путь (если есть) после основного эндпоинта;
                // method - метод запроса, его явно передаем в зависимости от контроллера;
                // requestEntity - сущность = это сам запрос его мы пересобрали в зависимости от входящих данных
                //Object.class - указываем класс объекта передаваемого хз зачем видимо для JSON

                //и в певом случае еще parameters - если забыл - см.контроллер)) = это список параметров адресной строки (которые после ?) идут =
                // = он сюда попадает путем считывания из контроллера и записи в переменные которые переданы сюда в метод;
                // далее тут их опять слепили как параметры в объект-запрос (rest) и по сути собственно отправили запрос с параметрами только уже на эндпоинт второй (на сервер)
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(shareitServerResponse); //то есть выше мы в этот объект записали результат работы сервера,
        // по сути какой-то объект (в том числе если ошибка); и тут хотим дообработать как-то ответ с сервера
    }

    private HttpHeaders defaultHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null) {
            headers.set(Headers.REQUESTER_ID, String.valueOf(userId));
        }
        return headers;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;  //если код 200 - то возвращаем объект-запрос обратно
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode()); //иначе - извлекли статус ответа и присвоили его билдеру

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody()); //если было тело то вернули его
        }

        return responseBuilder.build(); //не совсем понял че происходит)) общий смысл видимо определиться что вернуть?? метод вспомогательный применяется к каждому контроллеру
    }

}
