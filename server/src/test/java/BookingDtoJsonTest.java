import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = ShareItServer.class)
class BookingDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeAndDeserializeCreateBookingDto() throws Exception {
        CreateBookingDto dto = new CreateBookingDto();
        dto.setItemId(42L);
        dto.setStart(LocalDateTime.of(2026, 4, 20, 10, 0));
        dto.setEnd(LocalDateTime.of(2026, 4, 22, 18, 30));

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("\"itemId\":42");
        assertThat(json).contains("2026-04-20T10:00:00");

        CreateBookingDto deserialized = objectMapper.readValue(json, CreateBookingDto.class);
        assertThat(deserialized.getItemId()).isEqualTo(42L);
        assertThat(deserialized.getStart()).isEqualTo(dto.getStart());
    }
}