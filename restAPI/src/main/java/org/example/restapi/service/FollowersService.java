package org.example.restapi.service;

import org.example.restapi.data.FollowersEntity;
import org.example.restapi.data.UserEntity;
import org.example.restapi.exception.BadRequestException;
import org.example.restapi.exception.ConflictException;
import org.example.restapi.exception.EntityNotFoundException;
import org.example.restapi.model.FollowModel;
import org.example.restapi.repository.FollowersRepository;
import org.example.restapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FollowersService {
    private final FollowersRepository followersRepository;
    private final UserRepository userRepository;

    public FollowersService(FollowersRepository followersRepository,
                            UserRepository userRepository) {
        this.followersRepository = followersRepository;
        this.userRepository = userRepository;
    }

    public void follow(String follower, String followed) {
        if (follower.equals(followed)) {
            throw new BadRequestException("User " + follower + " cannot follow themselves.");
        }
        UserEntity followerEntity = userRepository.findByUsername(follower)
                .orElseThrow(() -> new EntityNotFoundException("User " + follower + " not found"));
        UserEntity followedEntity = userRepository.findByUsername(followed)
                .orElseThrow(() -> new EntityNotFoundException("User " + followed + " not found"));
        Optional<FollowersEntity> existingFriendship = followersRepository.findByFollowerIdAndFollowedId(followerEntity.getId(), followedEntity.getId());
        if (existingFriendship.isPresent()) {
            throw new ConflictException("User " + follower + " is already following user " + followed);
        }
        FollowersEntity friendship = new FollowersEntity(null, null, followerEntity, followedEntity);
        followersRepository.save(friendship);
    }

    public List<FollowModel> getUserFollowers(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User " + username + " not found"));
        List<FollowersEntity> followersEntities = followersRepository.findByFollowedId(user.getId());
        List<FollowModel> followModels = new ArrayList<>();
        for (FollowersEntity followersEntity : followersEntities) {
            followModels.add(mapToModel(followersEntity.getFollower(), followersEntity.getCreatedDate()));
        }
        return followModels;
    }

    public List<FollowModel> getUserFollowed(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User " + username + " not found"));
        List<FollowersEntity> followersEntities = followersRepository.findByFollowerId(user.getId());
        List<FollowModel> followModels = new ArrayList<>();
        for (FollowersEntity followersEntity : followersEntities) {
            followModels.add(mapToModel(followersEntity.getFollowed(), followersEntity.getCreatedDate()));
        }
        return followModels;
    }

    public void unfollow(String username1, String username2) {
        UserEntity user1 = userRepository.findByUsername(username1)
                .orElseThrow(() -> new EntityNotFoundException("User " + username1 + " not found"));
        UserEntity user2 = userRepository.findByUsername(username1)
                .orElseThrow(() -> new EntityNotFoundException("User " + username2 + " not found"));
        FollowersEntity followersEntity = followersRepository.findByFollowerIdAndFollowedId(user1.getId(), user2.getId())
                .orElseThrow(() -> new EntityNotFoundException(username1 + " doesn't follow " + username2));
        followersRepository.delete(followersEntity);
    }

    private FollowModel mapToModel(UserEntity userEntity, Date followDate) {
        FollowModel followModel = new FollowModel();
        followModel.setUsername(userEntity.getUsername());
        followModel.setEmail(userEntity.getEmail());
        followModel.setBirthdate(userEntity.getBirthDate().toInstant().toEpochMilli());
        followModel.setCreated(followDate.toInstant().toEpochMilli());
        return followModel;
    }
}
