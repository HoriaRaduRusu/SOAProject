package org.example.restapi.service;

import org.example.restapi.client.FaasClient;
import org.example.restapi.data.PostEntity;
import org.example.restapi.data.UserEntity;
import org.example.restapi.exception.BadRequestException;
import org.example.restapi.exception.EntityNotFoundException;
import org.example.restapi.model.PostModel;
import org.example.restapi.repository.PostRepository;
import org.example.restapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FaasClient faasClient;

    public PostService(UserRepository userRepository, PostRepository postRepository, FaasClient faasClient) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.faasClient = faasClient;
    }

    public PostModel getPost(Long id, boolean htmlBody) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + id + " not found"));
        return mapToModel(postEntity, htmlBody);
    }

    public List<PostModel> getPostsForUser(Long userId, boolean htmlBody) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }
        List<PostEntity> postEntities = postRepository.findByOwnerId(userId);
        return postEntities.stream()
                .map(post -> mapToModel(post, htmlBody))
                .collect(Collectors.toList());
    }

    public PostModel createPost(String username, PostModel postModel) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username " + username + " not found"));
        PostEntity postEntity = new PostEntity(null, postModel.getContent(), postModel.getTitle(), userEntity);
        postEntity = postRepository.save(postEntity);
        return mapToModel(postEntity, true);
    }

    public void deletePost(String username, Long id) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username " + username + " not found"));
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + id + " not found"));
        if (!postEntity.getOwner().getId().equals(userEntity.getId())) {
            throw new BadRequestException("User " + username + " does now own post with id " + id);
        }
        postRepository.delete(postEntity);
    }

    private PostModel mapToModel(PostEntity postEntity, boolean htmlBody) {
        PostModel postModel = new PostModel();
        postModel.setTitle(postEntity.getPostTitle());
        postModel.setAuthor(postEntity.getOwner().getUsername());
        postModel.setContent(htmlBody ? faasClient.convertMarkdownToHtml(postEntity.getPostBody()) : postEntity.getPostBody());
        return postModel;
    }
}
