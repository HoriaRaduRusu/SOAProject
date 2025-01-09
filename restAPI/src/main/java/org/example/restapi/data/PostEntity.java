package org.example.restapi.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "post_body")
    private String postBody;

    @Column(name = "post_title")
    private String postTitle;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;
}
