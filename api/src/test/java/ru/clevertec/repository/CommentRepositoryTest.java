package ru.clevertec.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.entity.CommentEntity;
import ru.clevertec.utils.CommentEntityFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@DataJpaTest
@ActiveProfiles(profiles = "test")
class CommentRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("test")
                    .withUsername("comment_user")
                    .withPassword("comment_pass");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void shouldSaveAndFindById() {
        // given
        CommentEntity comment = CommentEntityFactory.createDefaultComment();

        // when
        CommentEntity savedComment = commentRepository.save(comment);
        Optional<CommentEntity> foundComment = commentRepository.findById(savedComment.getId());

        // then
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getText()).isEqualTo("Default Comment Text");
    }

    @Test
    void shouldFindByNewsId() {
        // given
        UUID newsId = UUID.randomUUID();
        CommentEntity comment1 = CommentEntityFactory.createComment(newsId, "First comment");
        CommentEntity comment2 = CommentEntityFactory.createComment(newsId, "Second comment");

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        // when
        List<CommentEntity> comments = commentRepository.findByNewsId(newsId, Pageable.unpaged()).getContent();

        // then
        assertEquals(2, comments.size());
        assertThat(comments).extracting("text").containsExactlyInAnyOrder("First comment", "Second comment");
    }

    @Test
    void shouldDeleteById() {
        // given
        CommentEntity comment = CommentEntityFactory.createDefaultComment();
        CommentEntity savedComment = commentRepository.save(comment);

        // when
        commentRepository.deleteById(savedComment.getId());
        boolean exists = commentRepository.existsById(savedComment.getId());

        // then
        assertThat(exists).isFalse();
    }
}
