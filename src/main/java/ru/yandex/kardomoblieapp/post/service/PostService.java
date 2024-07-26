package ru.yandex.kardomoblieapp.post.service;

import org.springframework.web.multipart.MultipartFile;
import ru.yandex.kardomoblieapp.post.model.Post;

import java.util.List;

public interface PostService {
    Post createPost(long requesterId, MultipartFile file, String content);

    Post updatePost(long requesterId, long postId, MultipartFile file, String content);

    void deletePost(long requesterId, long postId);

    Post findPostById(long postId);

    List<Post> findPostsFromUser(long userId);
}
