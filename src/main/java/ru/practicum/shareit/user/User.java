package ru.practicum.shareit.user;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Getter //потом закоментить
    @Setter
    private static int userIdCounter = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    //@Getter
    private int id;
    @Column(name = "name", nullable = false)
    private String name;

    @Email //вроде работает
    @Column(name = "email", nullable = false, unique=true)
    private String email;

  //  @Transient
  //  private Set<Integer> userItemsToShare; //под это дело 3я таблица у меня, но это поле я вроде сам добавил, возможно оно при использовании БД не нужно; если такой функционал потребуется то его можно напрямую из БД получить
  @ElementCollection
  @CollectionTable(name="Items", joinColumns=@JoinColumn(name="owner_id")) //хз не уверен что правильно прописал; по смыслу тут список заполняется значениями колонки item_id таблицы items для того же owner_id что тут id юзера
  @Column(name="item_id")
    private Set<Integer> userItemsToShare = new HashSet<>();  //в БД такого поля нет, если что вернуть третью таблицу

    public User(String name, String email) {
        this.name = name;
        this.email = email;

     //   this.userItemsToShare = new HashSet<>();
       this.id = userIdCounter;
    }
}