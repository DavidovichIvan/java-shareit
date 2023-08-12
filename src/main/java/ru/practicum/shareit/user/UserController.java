package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService; //по идее тоже закоментить или хз мбыть и не надо; попробовал комененитить вроде работает

    @PostMapping
    public User add(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable("id") int id, @RequestBody User user) {
        user.setId(id);
        return userService.updateUser(id, user);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") int userId) {
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") int id) {
        userService.deleteById(id);
    }


//  @PatchMapping("/{itemId}")
//  public Item updItem(@PathVariable("itemId") int itemId,
    //                     @RequestHeader("X-Sharer-User-Id") int ownerId,
    //                     @RequestBody Item item) {
//      return itemService.updItem(itemId, ownerId, item);
//  }


    //  @Autowired     //или так или аннотация на конструктор сверху
    //public UserController(UserService userService) {
    //    this.userService = userService;
    // }

   /*
    @PostMapping                                                //автометод работает, пользователя добавляет в БД; почемуто-не возаращает только id пользователя; если этого будут требовать автотесты то наверное надо добавлять dto в который передаем юзера, присваиваем его id и возаращаем dto объект
    @ResponseStatus(HttpStatus.OK)
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PatchMapping("/{id}")                                  //тоже работает
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable("id") int id, @RequestBody User user) {
        user.setId(id);
        return userService.updateUser(user);
    }

    @GetMapping                                              //вроде работает автоматически
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")                                        //работает, тоже хочет видеть id в автотестах; мбыть в след коллекции этого нет
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable("id") int id) {
        return userService.getUser(id);
    }

    @DeleteMapping("/{id}")                                    //работает; связанные вещи тоже удаляет
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
    }
    */
}