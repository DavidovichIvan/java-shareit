package ru.practicum.shareit.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.practicum.shareit.exceptions.DataBaseException;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private final int ownerId = 99;

    private int itemId = 1;
    private String testName = "new Name";
    private String testDescription = "Updated";

    private Item itemMock = new Item(
            "TeaSet",
            "Teaset for twelve persons", ownerId);

    @Test
    void add() throws Exception {
        when(itemService.addItem(anyInt(), any()))
                .thenReturn(itemMock);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemMock))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemMock.getName())))
                .andExpect(jsonPath("$.description", is(itemMock.getDescription())));
    }

    @Test
    void updateItem() throws Exception {
        itemMock.setName(testName);
        itemMock.setDescription(testDescription);
        itemMock.setAvailable(true);
        itemMock.setId(itemId);

        when(itemService.updItem(anyInt(), anyInt(), any()))
                .thenReturn(itemMock);

        mvc.perform(patch("/items/{id}", itemMock.getId())
                        .content(mapper.writeValueAsString(itemMock))
                        .header("X-Sharer-User-Id", itemMock.getOwnerId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemMock.getName())))
                .andExpect(jsonPath("$.description", is(itemMock.getDescription())));
    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getItemById(anyInt(), anyInt()))
                .thenReturn(itemMock);
        itemMock.setId(itemId);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", itemMock.getOwnerId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemId)))
                .andExpect(jsonPath("$.name", is(itemMock.getName())))
                .andExpect(jsonPath("$.description", is(itemMock.getDescription())));
    }

    @Test
    void searchItem() throws Exception {
        when(itemService.searchItem(anyString(), anyString()))
                .thenReturn(List.of(itemMock));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is("Teaset for twelve persons")));
    }

    @Test
    void addComment() throws Exception {
        Comment testComment = new Comment();
        //    testComment.setItemId(1);
        testComment.setText("Тестовый комментарий");

        when(itemService.addComment(anyInt(), anyInt(), anyString()))
                .thenReturn(testComment);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(testComment))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is("Тестовый комментарий")));
    }

    @Test
    void getItemByIdWithException() throws Exception {
        when(itemService.getItemById(-1, -1))
                .thenThrow(DataBaseException.class);

        mvc.perform(get("/items/-1")
                        .header("X-Sharer-User-Id", -1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(400));
    }
}