package org.example.restapi.service;

import org.example.restapi.client.FaasClient;
import org.example.restapi.data.UserEntity;
import org.example.restapi.exception.EntityNotFoundException;
import org.example.restapi.model.UserModel;
import org.example.restapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final FaasClient faasClient;

    public UserService(UserRepository userRepository,
                       FaasClient faasClient) {
        this.userRepository = userRepository;
        this.faasClient = faasClient;
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToModel)
                .collect(Collectors.toList());
    }

    public UserModel getUser(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User " + username + " not found"));
        userEntity.setMainPage(faasClient.convertMarkdownToHtml(userEntity.getMainPage()));
        return mapToModel(userEntity);
    }

    @Transactional
    public UserModel updateUser(String username, String newMainPage) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User " + username + " not found"));
        userEntity.setMainPage(newMainPage);
        userEntity = userRepository.save(userEntity);
        userEntity.setMainPage(faasClient.convertMarkdownToHtml(userEntity.getMainPage()));
        return mapToModel(userEntity);
    }

    public UserModel mapToModel(UserEntity userEntity) {
        UserModel userModel = new UserModel();
        userModel.setUsername(userEntity.getUsername());
        userModel.setEmail(userEntity.getEmail());
        userModel.setMainPage(userEntity.getMainPage());
        userModel.setBirthDate(userEntity.getBirthDate().toInstant().toEpochMilli());
        return userModel;
    }
}
