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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "friends")
public class FriendsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, insertable = false, updatable = false, name = "friend_A_id")
    private UUID friendAId;

    @Column(nullable = false, insertable = false, updatable = false, name = "friend_B_id")
    private UUID friendBId;

    @Column(nullable = false, insertable = false, updatable = false, name = "request_id")
    private UUID requestId;

    @CreationTimestamp
    @Column(nullable = false, name = "friendship_at")
    private Date friendshipAt;

    @Column(nullable = false, name = "is_friendship_deleted")
    private Boolean isFriendshipDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_A_id")
    private UserEntity friendA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_B_id")
    private UserEntity friendB;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private RequestsEntity request;

    public FriendsEntity() {
    }

    public FriendsEntity(UserEntity _friendA, UserEntity _friendB, RequestsEntity _request) {
        this.friendA = _friendA;
        this.friendB = _friendB;
        this.request = _request;
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

    public void setIsFriendshipDeleted(boolean _isDeleted) {
        this.isFriendshipDeleted = _isDeleted;
    }

    public void setRequest(RequestsEntity _request) {
        this.request = _request;
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

    public UUID getRequestId() {
        return (request != null) ? request.getId() : null;
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

    public RequestsEntity getRequest() {
        return request;
    }

    public boolean getIsFriendshipDeleted() {
        return isFriendshipDeleted;
    }
}
