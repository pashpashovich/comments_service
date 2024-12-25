package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import ru.clevertec.domain.Comment;
import ru.clevertec.entity.CommentEntity;

@Mapper(componentModel = "spring")
public interface CommentsDomainMapper {

    default Page<Comment> toDomainList(Page<CommentEntity> comments) {
        return comments.map(this::toDomain);
    }

    Comment toDomain(CommentEntity comment);

    CommentEntity toEntity(Comment comment);

}
