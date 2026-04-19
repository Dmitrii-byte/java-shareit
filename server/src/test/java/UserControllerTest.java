import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = UserController.class)
@ContextConfiguration(classes = ShareItServer.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAllUsers_shouldReturnOk() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getUserById_shouldReturnOk() throws Exception {
        Mockito.when(userService.getUserById(1L)).thenReturn(Optional.of(new UserDto()));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void saveUser_shouldReturnCreated() throws Exception {
        UserDto dto = new UserDto(0L, "test@mail.ru", "Test");
        Mockito.when(userService.saveUser(ArgumentMatchers.any(UserDto.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void updateUser_shouldReturnOk() throws Exception {
        UserDtoUpdate update = new UserDtoUpdate();
        update.setName("New Name");
        Mockito.when(userService.updateUser(ArgumentMatchers.eq(1L), ArgumentMatchers.any(UserDtoUpdate.class))).thenReturn(new UserDto());

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteUser_shouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}