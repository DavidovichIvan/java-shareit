package ru.practicum.shareit.Serviсes;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Data
public class ItemServiceTest {

    private final ItemService itemService;

    private final int testId = 1;
    private final int ownerId = 1;
    private final int requesterId = 1;

    private int amountOfItemsInDB;

    private List<Item> itemList = new ArrayList();

    Item testItem = new Item("TestItem", "TestDescription", ownerId);


    @BeforeEach
    public void before() {
        amountOfItemsInDB = itemService.getItemRepository().findAll().size();
        log.info("Количество вещей в БД = " + amountOfItemsInDB);
    }

    @AfterEach
    public void clean() {
        itemList.clear();
    }
/*
    @Test
    public void getUserItems() {
        testItem.setAvailable(true);
        itemService.addItem(ownerId, testItem);

        itemList = itemService.getUserItems(ownerId);

        assertThat(!itemList.isEmpty());
        assertThat(itemList.contains(testItem));
    }

    @Test
    public void getItemById() {
        testItem.setAvailable(true);
        itemService.addItem(ownerId, testItem);

        Item item = itemService.getItemById(requesterId, testItem.getId());
        assertThat(item.getName().equals("TestItem"));
        assertThat(item.getDescription().equals("TestDescription"));
    }
 */

    @Test
    public void searchItemByText() {
        assertThat(itemList.isEmpty());
        itemList = itemService.searchItem("Boat", "boAt");
        assertThat(!itemList.isEmpty());

        for (int i = 0; i < itemList.size(); i++) {
            assertThat(itemList.get(i).getName().toLowerCase().contains("boat")
                    || itemList.get(i).getDescription().toLowerCase().contains("boat"));
        }
    }

}