package org.example.authenticationserver.service;

import io.jsonwebtoken.Jwts;
import org.example.authenticationserver.data.UserEntity;
import org.example.authenticationserver.exception.AttributeInUseException;
import org.example.authenticationserver.exception.InvalidLoginException;
import org.example.authenticationserver.model.JwkSetResponseDTO;
import org.example.authenticationserver.model.JwtResponseDTO;
import org.example.authenticationserver.model.RegisterUserDTO;
import org.example.authenticationserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    @Value("${jwt.expiration}")
    private long expiration;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public JwtResponseDTO authenticate(String email, String password) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidLoginException("User not found"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            return new JwtResponseDTO(generateToken(user.getUsername()));
        }
        throw new InvalidLoginException("Invalid password");
    }

    public JwkSetResponseDTO getJwks() {
        JwkSetResponseDTO.JwkResponseDTO responseDTO = JwkSetResponseDTO.JwkResponseDTO.builder()
                .kty(publicKey.getAlgorithm())
                .n(Base64.getUrlEncoder().encodeToString(publicKey.getModulus().toByteArray()))
                .e(Base64.getUrlEncoder().encodeToString(publicKey.getPublicExponent().toByteArray()))
                .alg("RS512")
                .use("sig")
                .build();
        return new JwkSetResponseDTO(List.of(responseDTO));
    }

    public JwtResponseDTO register(RegisterUserDTO registerUserDTO) {
        if (userRepository.existsByEmail(registerUserDTO.getEmail())) {
            throw new AttributeInUseException("Email already in use");
        }
        if (userRepository.existsByUsername(registerUserDTO.getUsername())) {
            throw new AttributeInUseException("Username already in use");
        }
        UserEntity user = mapFromDto(registerUserDTO);
        userRepository.save(user);
        return new JwtResponseDTO(generateToken(user.getUsername()));
    }

    private String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(privateKey, Jwts.SIG.RS512)
                .compact();
    }

    private UserEntity mapFromDto(RegisterUserDTO registerUserDTO) {
        UserEntity user = new UserEntity();
        user.setUsername(registerUserDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        user.setEmail(registerUserDTO.getEmail());
        user.setBirthDate(registerUserDTO.getBirthDate());
        return user;
    }
}
