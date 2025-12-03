package org.riwi.eventcatalog.infraestructura.adapters.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.riwi.eventcatalog.AbstractIntegrationTest;
import org.riwi.eventcatalog.dominio.model.Role;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.CategoryRequest;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.EventRequest;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.LoginRequest;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.RegisterRequest;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.EventRequest.VenueRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate; // Importar JdbcTemplate
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate; // Inyectar JdbcTemplate

    private String adminToken;
    private String userToken;
    private String venueId;
    private String categoryId;

    @BeforeEach
    void setUp() throws Exception {
        // Clear tables before each test to ensure a clean state
        jdbcTemplate.update("DELETE FROM event_category");
        jdbcTemplate.update("DELETE FROM event");
        jdbcTemplate.update("DELETE FROM venue");
        jdbcTemplate.update("DELETE FROM category");
        jdbcTemplate.update("DELETE FROM app_user");

        // Register and login ADMIN user
        RegisterRequest adminRegister = new RegisterRequest();
        adminRegister.setUsername("admin_event_test_" + UUID.randomUUID()); // Unique username
        adminRegister.setPassword("password");
        adminRegister.setRole(Role.ADMIN);
        MvcResult adminResult = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminRegister)))
                .andExpect(status().isOk())
                .andReturn();
        adminToken = objectMapper.readTree(adminResult.getResponse().getContentAsString()).get("token").asText();

        // Register and login USER user
        RegisterRequest userRegister = new RegisterRequest();
        userRegister.setUsername("user_event_test_" + UUID.randomUUID()); // Unique username
        userRegister.setPassword("password");
        userRegister.setRole(Role.USER);
        MvcResult userResult = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegister)))
                .andExpect(status().isOk())
                .andReturn();
        userToken = objectMapper.readTree(userResult.getResponse().getContentAsString()).get("token").asText();

        // Create a Venue (ADMIN only)
        String venueName = "Test Venue " + UUID.randomUUID();
        String venueJson = "{\"name\": \"" + venueName + "\", \"city\": \"Test City\", \"capacity\": 1000}";
        MvcResult venueMvcResult = mockMvc.perform(post("/venues")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(venueJson))
                .andExpect(status().isOk())
                .andReturn();
        venueId = objectMapper.readTree(venueMvcResult.getResponse().getContentAsString()).get("id").asText();

        // Insert a Category directly into the database
        categoryId = UUID.randomUUID().toString();
        String categoryName = "Test Category " + UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO category (id, name) VALUES (?, ?)", categoryId, categoryName);
    }

    private EventRequest createEventRequest(String name) {
        EventRequest eventRequest = new EventRequest();
        eventRequest.setName(name);
        eventRequest.setDate(LocalDate.now().plusDays(10));
        eventRequest.setDescription("Description for " + name);
        eventRequest.setStatus(org.riwi.eventcatalog.dominio.model.EventStatus.ACTIVE);

        CategoryRequest catReq = new CategoryRequest();
        catReq.setId(categoryId);
        eventRequest.setCategories(Set.of(catReq));

        VenueRequest venReq = new VenueRequest();
        venReq.setId(venueId);
        eventRequest.setVenue(venReq);
        return eventRequest;
    }

    @Test
    void createEvent_asAdmin_shouldReturnOk() throws Exception {
        EventRequest eventRequest = createEventRequest("Admin Event " + UUID.randomUUID());

        mockMvc.perform(post("/events")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(eventRequest.getName()));
    }

    @Test
    void createEvent_asUser_shouldReturnForbidden() throws Exception {
        EventRequest eventRequest = createEventRequest("User Attempt Event " + UUID.randomUUID());

        mockMvc.perform(post("/events")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Acceso denegado. No tiene permisos para realizar esta acción."));
    }

    @Test
    void createEvent_withoutToken_shouldReturnForbidden() throws Exception {
        EventRequest eventRequest = createEventRequest("No Token Event " + UUID.randomUUID());

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getEventById_asUser_shouldReturnOk() throws Exception {
        EventRequest eventRequest = createEventRequest("Fetchable Event " + UUID.randomUUID());
        MvcResult result = mockMvc.perform(post("/events")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String eventId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(get("/events/{id}", eventId)
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(eventRequest.getName()));
    }

    @Test
    void getEventById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/events/{id}", "non-existent-id")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Event con id non-existent-id no encontrado"));
    }

    @Test
    void updateEvent_asAdmin_shouldReturnOk() throws Exception {
        EventRequest eventRequest = createEventRequest("Updatable Event " + UUID.randomUUID());
        MvcResult result = mockMvc.perform(post("/events")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String eventId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();

        eventRequest.setName("Updated Event Name " + UUID.randomUUID());
        mockMvc.perform(put("/events/{id}", eventId)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(eventRequest.getName()));
    }

    @Test
    void updateEvent_asUser_shouldReturnForbidden() throws Exception {
        EventRequest eventRequest = createEventRequest("Update Forbidden Event " + UUID.randomUUID());
        MvcResult result = mockMvc.perform(post("/events")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String eventId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();

        eventRequest.setName("Attempted Update Name " + UUID.randomUUID());
        mockMvc.perform(put("/events/{id}", eventId)
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteEvent_asAdmin_shouldReturnNoContent() throws Exception {
        EventRequest eventRequest = createEventRequest("Deletable Event " + UUID.randomUUID());
        MvcResult result = mockMvc.perform(post("/events")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String eventId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(delete("/events/{id}", eventId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        // Verify it's actually deleted
        mockMvc.perform(get("/events/{id}", eventId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEvent_asUser_shouldReturnForbidden() throws Exception {
        EventRequest eventRequest = createEventRequest("Delete Forbidden Event " + UUID.randomUUID());
        MvcResult result = mockMvc.perform(post("/events")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String eventId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(delete("/events/{id}", eventId)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }
}
