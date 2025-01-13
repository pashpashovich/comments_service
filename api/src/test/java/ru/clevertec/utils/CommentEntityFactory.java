package ru.clevertec.utils;


import ru.clevertec.entity.CommentEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public class CommentEntityFactory {

    public static CommentEntity createComment(UUID newsId, String text) {
        CommentEntity comment = new CommentEntity();
        comment.setText(text);
        comment.setUsername("pashpashovich");
        comment.setNewsId(newsId);
        comment.setCreatedAt(LocalDateTime.now());
        return comment;
    }

    public static CommentEntity createDefaultComment() {
        return createComment(UUID.randomUUID(), "Default Comment Text");
    }
}

