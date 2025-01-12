package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ru.clevertec.domain.Comment;
import ru.clevertec.domain.CommentCreateRequestFromDto;
import ru.clevertec.domain.CommentFromDto;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    default Page<CommentFromDto> toDtoList(Page<Comment> comments) {
        return comments.map(this::toDto);
    }


    CommentFromDto toDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "username",ignore = true)
    Comment toDomain(CommentCreateRequestFromDto createRequest);
}
