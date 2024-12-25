package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ru.clevertec.domain.Comment;
import ru.clevertec.dto.CommentCreateRequest;
import ru.clevertec.dto.CommentDto;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    default Page<CommentDto> toDtoList(Page<Comment> comments) {
        return comments.map(this::toDto);
    }


    CommentDto toDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment toDomain(CommentCreateRequest createRequest);
}
