package org.riwi.eventcatalog.infraestructura.adapters.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.riwi.eventcatalog.AbstractIntegrationTest;
import org.riwi.eventcatalog.dominio.model.Role;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.LoginRequest;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.RegisterRequest;
import org.riwi.eventcatalog.infraestructura.adapters.in.web.dto.VenueRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VenueControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String adminToken;
    private String userToken;

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
        adminRegister.setUsername("admin_venue_test_" + UUID.randomUUID()); // Unique username
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
        userRegister.setUsername("user_venue_test_" + UUID.randomUUID()); // Unique username
        userRegister.setPassword("password");
        userRegister.setRole(Role.USER);
        MvcResult userResult = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegister)))
                .andExpect(status().isOk())
                .andReturn();
        userToken = objectMapper.readTree(userResult.getResponse().getContentAsString()).get("token").asText();
    }

    private VenueRequest createVenueRequest(String name) {
        VenueRequest venueRequest = new VenueRequest();
        venueRequest.setName(name);
        venueRequest.setCity("Test City");
        venueRequest.setCapacity(500);
        return venueRequest;
    }

    @Test
    void createVenue_asAdmin_shouldReturnOk() throws Exception {
        VenueRequest venueRequest = createVenueRequest("Admin Venue " + UUID.randomUUID());

        mockMvc.perform(post("/venues")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venueRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(venueRequest.getName()));
    }

    @Test
    void createVenue_asUser_shouldReturnForbidden() throws Exception {
        VenueRequest venueRequest = createVenueRequest("User Attempt Venue " + UUID.randomUUID());

        mockMvc.perform(post("/venues")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venueRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Acceso denegado. No tiene permisos para realizar esta acción."));
    }

    @Test
    void createVenue_withoutToken_shouldReturnForbidden() throws Exception {
        VenueRequest venueRequest = createVenueRequest("No Token Venue " + UUID.randomUUID());

        mockMvc.perform(post("/venues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venueRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllVenues_asUser_shouldReturnOk() throws Exception {
        // Create a venue as admin first
        VenueRequest venueRequest = createVenueRequest("Fetchable Venue " + UUID.randomUUID());
        mockMvc.perform(post("/venues")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venueRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/venues")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(venueRequest.getName()));
    }

    @Test
    void getVenueById_asUser_shouldReturnOk() throws Exception {
        VenueRequest venueRequest = createVenueRequest("Specific Venue " + UUID.randomUUID());
        MvcResult result = mockMvc.perform(post("/venues")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venueRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String venueId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(get("/venues/{id}", venueId)
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(venueRequest.getName()));
    }

    @Test
    void getVenueById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/venues/{id}", "non-existent-id")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Venue con id non-existent-id no encontrado"));
    }

    @Test
    void updateVenue_asAdmin_shouldReturnOk() throws Exception {
        VenueRequest venueRequest = createVenueRequest("Updatable Venue " + UUID.randomUUID());
        MvcResult result = mockMvc.perform(post("/venues")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venueRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String venueId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();

        venueRequest.setName("Updated Venue Name " + UUID.randomUUID());
        mockMvc.perform(put("/venues/{id}", venueId)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venueRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(venueRequest.getName()));
    }

    @Test
    void updateVenue_asUser_shouldReturnForbidden() throws Exception {
        VenueRequest venueRequest = createVenueRequest("Update Forbidden Venue " + UUID.randomUUID());
        MvcResult result = mockMvc.perform(post("/venues")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venueRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String venueId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();

        venueRequest.setName("Attempted Update Name " + UUID.randomUUID());
        mockMvc.perform(put("/venues/{id}", venueId)
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venueRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteVenue_asAdmin_shouldReturnNoContent() throws Exception {
        VenueRequest venueRequest = createVenueRequest("Deletable Venue " + UUID.randomUUID());
        MvcResult result = mockMvc.perform(post("/venues")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venueRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String venueId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(delete("/venues/{id}", venueId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        // Verify it's actually deleted
        mockMvc.perform(get("/venues/{id}", venueId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteVenue_asUser_shouldReturnForbidden() throws Exception {
        VenueRequest venueRequest = createVenueRequest("Delete Forbidden Venue " + UUID.randomUUID());
        MvcResult result = mockMvc.perform(post("/venues")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venueRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String venueId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(delete("/venues/{id}", venueId)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }
}
