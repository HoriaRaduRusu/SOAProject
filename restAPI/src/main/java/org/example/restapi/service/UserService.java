package org.example.restapi.service;

import org.example.restapi.data.UserEntity;
import org.example.restapi.exception.EntityNotFoundException;
import org.example.restapi.model.UserModel;
import org.example.restapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToModel)
                .collect(Collectors.toList());
    }

    public UserModel getUser(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User " + username + " not found"));
        return mapToModel(userEntity);
    }

    public UserModel mapToModel(UserEntity userEntity) {
        UserModel userModel = new UserModel();
        userModel.setUsername(userEntity.getUsername());
        userModel.setEmail(userEntity.getEmail());
        userModel.setBirthDate(userEntity.getBirthDate().toInstant().toEpochMilli());
        return userModel;
    }
}
