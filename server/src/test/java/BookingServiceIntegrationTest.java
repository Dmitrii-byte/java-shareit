import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ru.practicum.shareit.ShareItServer.class)
@ActiveProfiles("test")
class BookingServiceIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void createBooking_shouldSaveAndReturnDto() {
        User owner = userRepository.save(new User(null, "owner", "owner@mail.ru"));
        User booker = userRepository.save(new User(null, "booker", "booker@mail.ru"));
        Item item = itemRepository.save(new Item(null, owner, "Вещь", "Описание", true, null));

        CreateBookingDto dto = new CreateBookingDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(3));

        BookingDto created = bookingService.createBooking(booker.getId(), dto);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getStatus()).isEqualTo(Status.WAITING);
        assertThat(created.getBooker().getId()).isEqualTo(booker.getId());
    }
}