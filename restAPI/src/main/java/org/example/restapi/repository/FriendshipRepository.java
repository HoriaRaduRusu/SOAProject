package org.example.restapi.repository;

import org.example.restapi.data.FriendshipEntity;
import org.example.restapi.data.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Long> {
    @Query(value = "select f from FriendshipEntity f where f.user1.id = :userId or f.user2.id = :userId")
    List<FriendshipEntity> findByUserId(Long userId);
    @Query(value = "select f From FriendshipEntity f where (f.user1.id = :user1Id and f.user2.id = :user2Id) or (f.user1.id = :user2Id and f.user2.id = :user1Id)")
    Optional<FriendshipEntity> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);
}
