package org.example.authenticationserver.data;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "users")
@SecondaryTable(name = "user_authentication_details", pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id"))
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "password", table = "user_authentication_details")
    private String password;
}
