package org.example.restapi.repository;

import org.example.restapi.data.FollowersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowersRepository extends JpaRepository<FollowersEntity, Long> {
    List<FollowersEntity> findByFollowerId(Long id);
    List<FollowersEntity> findByFollowedId(Long id);
    Optional<FollowersEntity> findByFollowerIdAndFollowedId(Long followerId, Long followedId);
}
