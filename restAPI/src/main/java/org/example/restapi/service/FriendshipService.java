package org.example.restapi.service;

import org.example.restapi.data.FriendshipEntity;
import org.example.restapi.data.UserEntity;
import org.example.restapi.exception.BadRequestException;
import org.example.restapi.exception.ConflictException;
import org.example.restapi.exception.EntityNotFoundException;
import org.example.restapi.model.FriendshipModel;
import org.example.restapi.repository.FriendshipRepository;
import org.example.restapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    public FriendshipService(FriendshipRepository friendshipRepository,
                             UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    public void createFriendship(String username1, String username2) {
        if (username1.equals(username2)) {
            throw new BadRequestException("User " + username1 + " cannot be friends with themselves");
        }
        UserEntity user1 = userRepository.findByUsername(username1)
                .orElseThrow(() -> new EntityNotFoundException("User " + username1 + " not found"));
        UserEntity user2 = userRepository.findByUsername(username1)
                .orElseThrow(() -> new EntityNotFoundException("User " + username2 + " not found"));
        Optional<FriendshipEntity> existingFriendship = friendshipRepository.findByUser1IdAndUser2Id(user1.getId(), user2.getId());
        if (existingFriendship.isPresent()) {
            throw new ConflictException("User " + username1 + " is already friends with user " + username2);
        }
        FriendshipEntity friendship = new FriendshipEntity(null, null, user1, user2);
        friendshipRepository.save(friendship);
    }

    public List<FriendshipModel> getUserFriends(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User " + username + " not found"));
        List<FriendshipEntity> friendshipEntities = friendshipRepository.findByUserId(user.getId());
        List<FriendshipModel> friendshipModels = new ArrayList<>();
        for (FriendshipEntity friendshipEntity : friendshipEntities) {
            UserEntity friend = friendshipEntity.getUser1().getUsername().equals(username) ? friendshipEntity.getUser2() : friendshipEntity.getUser1();
            FriendshipModel friendshipModel = new FriendshipModel();
            friendshipModel.setUsername(friend.getUsername());
            friendshipModel.setEmail(friend.getEmail());
            friendshipModel.setCreated(friendshipEntity.getCreatedDate().toInstant().toEpochMilli());
            friendshipModel.setBirthdate(friend.getBirthDate().toInstant().toEpochMilli());
            friendshipModels.add(friendshipModel);
        }
        return friendshipModels;
    }

    public void deleteFriendship(String username1, String username2) {
        UserEntity user1 = userRepository.findByUsername(username1)
                .orElseThrow(() -> new EntityNotFoundException("User " + username1 + " not found"));
        UserEntity user2 = userRepository.findByUsername(username1)
                .orElseThrow(() -> new EntityNotFoundException("User " + username2 + " not found"));
        FriendshipEntity friendshipEntity = friendshipRepository.findByUser1IdAndUser2Id(user1.getId(), user2.getId())
                .orElseThrow(() -> new EntityNotFoundException("Friendship between " + username1 + " and " + username2 + " not found"));
        friendshipRepository.delete(friendshipEntity);
    }
}
