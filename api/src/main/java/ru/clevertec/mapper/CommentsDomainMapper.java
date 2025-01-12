package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import ru.clevertec.domain.Comment;
import ru.clevertec.domain.CommentCreateRequestFromDto;
import ru.clevertec.domain.CommentFromDto;
import ru.clevertec.domain.NewsFromDto;
import ru.clevertec.dto.CommentCreateRequest;
import ru.clevertec.dto.CommentDto;
import ru.clevertec.dto.NewsDto;
import ru.clevertec.entity.CommentEntity;


@Mapper(componentModel = "spring")
public interface CommentsDomainMapper {

    default Page<Comment> toDomainList(Page<CommentEntity> comments) {
        return comments.map(this::toDomain);
    }

    Comment toDomain(CommentEntity comment);

    CommentEntity toEntity(Comment comment);

    CommentDto toDto(CommentFromDto commentFromDto);

    default Page<CommentDto> toDtoPage(Page<CommentFromDto> comments) {
        return comments.map(this::toDto);
    }

    CommentCreateRequestFromDto toCommentCreateRequestFromDto(CommentCreateRequest commentCreateRequest);

    NewsFromDto toNewsFromDto(NewsDto newsDto);
}
