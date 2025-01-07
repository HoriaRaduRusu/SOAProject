package org.example.restapi.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "friends")
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_date")
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id_1")
    private UserEntity user1;

    @ManyToOne
    @JoinColumn(name = "user_id_2")
    private UserEntity user2;
}
