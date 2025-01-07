package org.example.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipModel {
    private String username;
    private String email;
    private Long birthdate;
    private Long created;
}
