package ru.clevertec.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.domain.CommentCreateRequestFromDto;
import ru.clevertec.domain.CommentFromDto;
import ru.clevertec.service.CommentService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.3")
            .withDatabaseName("test")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    @WithMockUser()
    void shouldReturnAllComments() throws Exception {
        // given
        when(commentService.getAllComments(any())).thenReturn(Page.empty());
        //when
        //then
        mockMvc.perform(get("/comments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isEmpty())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Все комментарии получены"));
    }

    @Test
    @WithMockUser()
    void shouldReturnCommentById() throws Exception {
        // given
        UUID commentId = UUID.randomUUID();
        CommentFromDto mockComment = CommentFromDto.builder()
                .id(commentId)
                .text("Comment Text")
                .username("user")
                .createdAt(LocalDateTime.now())
                .build();
        when(commentService.getCommentById(commentId)).thenReturn(mockComment);
        //when
        //then
        mockMvc.perform(get("/comments/by-id/{commentId}", commentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.text").value("Comment Text"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Получен комментарий"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldUpdateTextInComment() throws Exception {
        // given
        UUID commentId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        CommentFromDto mockServiceResponse = new CommentFromDto(commentId, "Updated text", "user1", now);

        when(commentService.updateTextInComment(eq(commentId), any(String.class))).thenReturn(mockServiceResponse);
        //when
        //then
        mockMvc.perform(patch("/comments/{id}", commentId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"Updated text\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.text").value("Updated text"));
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldCreateComment() throws Exception {
        // given
        CommentFromDto mockComment = CommentFromDto.builder()
                .text("Created Comment")
                .build();
        when(commentService.createComment(any(CommentCreateRequestFromDto.class))).thenReturn(mockComment);
        //when
        //then
        mockMvc.perform(post("/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "text": "Created Comment",
                                  "newsId": "7010546e-b7e3-47c5-806e-a87aaf971cd6"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.text").value("Created Comment"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Комментарий успешно создан"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldUpdateComment() throws Exception {
        // given
        UUID commentId = UUID.randomUUID();
        CommentFromDto updatedComment = CommentFromDto.builder()
                .text("Updated Comment")
                .build();

        when(commentService.updateComment(eq(commentId), any(CommentCreateRequestFromDto.class))).thenReturn(updatedComment);
        //when
        //then
        mockMvc.perform(put("/comments/{commentId}", commentId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "text": "Updated Comment",
                                  "newsId": "7010546e-b7e3-47c5-806e-a87aaf971cd6"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.text").value("Updated Comment"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Комментарий обновлен"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldDeleteComment() throws Exception {
        // given
        UUID commentId = UUID.randomUUID();
        //when
        //then
        mockMvc.perform(delete("/comments/{commentId}", commentId)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful());
    }
}
