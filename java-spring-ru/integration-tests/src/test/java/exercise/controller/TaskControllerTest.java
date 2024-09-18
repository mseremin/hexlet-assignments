package exercise.controller;

import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

// BEGIN
@AutoConfigureMockMvc
@SpringBootTest
// END
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    private Task generateTask() {
        return Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
                .supply(Select.field(Task::getDescription), () -> faker.lorem().paragraph())
                .create();
    }

    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    // BEGIN
    @Test
    public void testGetTask() throws Exception {
        var task = generateTask();
        taskRepository.save(task);
        mockMvc.perform(get("/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(task.getTitle()))
                .andExpect(jsonPath("description").value(task.getDescription()))
                .andReturn();
    }

    @Test
    public void testCreateTask() throws Exception {
        var data = new HashMap<>();
        var title = "postTitle";
        var desc = "postDesc";
        data.put("title", title);
        data.put("description", desc);
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data)))
                .andExpect(status().isCreated())
                .andReturn();
        var task = taskRepository.findByTitle(title).get();
        assertThat(task.getDescription()).isEqualTo(desc);
    }


    @Test
    public void testUpdateTask() throws Exception {
        var task = generateTask();
        taskRepository.save(task);
        var newTitle = "NEW_TITLE";
        var newDesc = "NEW_DESC";
        var data = new HashMap<>();
        data.put("title", newTitle);
        data.put("description", newDesc);
        mockMvc.perform(put("/tasks/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data)))
                .andExpect(status().isOk())
                .andReturn();
        var updatedTask = taskRepository.findById(task.getId()).get();
        assertThat(updatedTask.getDescription()).isEqualTo(newDesc);
        assertThat(updatedTask.getTitle()).isEqualTo(newTitle);
    }

    @Test
    public void testDeleteTask() throws Exception {
        var task = generateTask();
        taskRepository.save(task);
        mockMvc.perform(delete("/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andReturn();
        var deletedTask = taskRepository.findById(task.getId());
        assertThat(deletedTask.isPresent()).isFalse();
    }
    // END
}
