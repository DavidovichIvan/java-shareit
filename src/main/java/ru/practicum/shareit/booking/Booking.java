package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor
public class Booking {

//   private final String DEFAULT_BOOKING_STATUS = String.valueOf(BookingState.WAITING);

    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Column(name = "booker_id", nullable = false)
    private Integer bookerId;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;                    //тут могут быть несостыковки (Instant, Date, LocalDate)

    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;

    // @Column(name = "status")
    // @Enumerated(EnumType.STRING)
    // private BookingState status = BookingState.WAITING;

    @Column(name = "status")
    private String status = "WAITING";

  //  @Transient  //наверное лучше подтягивать объединением данные из другой таблицы по id
    // -- видимо через связи; посомтреть лекции и https://java-online.ru/hibernate-entities.xhtml
            //если получится, то убрать в методах валидации добавление
 //   Item item;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    private Item item;


    @OneToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="booker_id", insertable = false, updatable = false)
    private User booker;
//joinColumns=@JoinColumn(name="owner_id")
}
