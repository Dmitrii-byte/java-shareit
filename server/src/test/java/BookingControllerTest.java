import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@ContextConfiguration(classes = ShareItServer.class)
class BookingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookingService bookingService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createBooking_shouldReturnCreated() throws Exception {
        CreateBookingDto dto = new CreateBookingDto();
        dto.setItemId(1L);
        dto.setStart(java.time.LocalDateTime.now().plusDays(1));
        dto.setEnd(java.time.LocalDateTime.now().plusDays(3));

        when(bookingService.createBooking(eq(1L), any(CreateBookingDto.class)))
                .thenReturn(new BookingDto());

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void approve_shouldReturnOk() throws Exception {
        when(bookingService.approveOrRejectBooking(1L, 10L, true))
                .thenReturn(new BookingDto());

        mockMvc.perform(patch("/bookings/10")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/bookings/10")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getBookerBookings_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void getOwnerBookings_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }
}