package ru.yandex.kardomoblieapp.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.kardomoblieapp.datafiles.dto.DataFileDto;
import ru.yandex.kardomoblieapp.user.dto.ShortUserDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {

    private long id;

    private String title;

    private ShortUserDto author;

    private DataFileDto file;

    private long likes;

    private long views;

    private List<CommentDto> comments;
}
