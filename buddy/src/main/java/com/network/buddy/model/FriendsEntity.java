package com.network.buddy.model;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "friends")
public class FriendsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, insertable = false, updatable = false, name = "friend_A_id")
    private UUID friendAId;

    @Column(nullable = false, insertable = false, updatable = false, name = "friend_B_id")
    private UUID friendBId;

    @CreationTimestamp
    @Column(nullable = false, name = "friendship_at")
    private Date friendshipAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "friend_A_id")
    private UserEntity friendA;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "friend_B_id")
    private UserEntity friendB;

    public FriendsEntity() {
    }

    public FriendsEntity(UserEntity _friendA, UserEntity _friendB) {
        this.friendA = _friendA;
        this.friendB = _friendB;
    }

    public void setId(UUID _id) {
        this.id = _id;
    }

    public void setFriendA(UserEntity _friendA) {
        this.friendA = _friendA;
    }

    public void setFriendB(UserEntity _friendB) {
        this.friendB = _friendB;
    }

    public UUID getId() {
        return id;
    }

    public UUID getFriendAId() {
        return (friendA != null) ? friendA.getId() : null;
    }

    public UUID getFriendBId() {
        return (friendB != null) ? friendB.getId() : null;
    }

    public Date getFriendshiAt() {
        return friendshipAt;
    }

    public UserEntity getFriendA() {
        return friendA;
    }

    public UserEntity getFriendB() {
        return friendB;
    }
}
