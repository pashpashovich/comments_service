package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import ru.clevertec.dto.CommentDto;
import ru.clevertec.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentsMapper {

    default Page<CommentDto> toDtoList(Page<Comment> comments) {
        return comments.map(this::toDto);
    }


    CommentDto toDto(Comment comment);

}
