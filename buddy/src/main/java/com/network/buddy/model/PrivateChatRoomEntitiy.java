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
@Table(name = "private_chatrooms")
public class PrivateChatRoomEntitiy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, name = "sender_id", insertable = false, updatable = false)
    private UUID senderId;

    @Column(nullable = false, name = "receiver_id", insertable = false, updatable = false)
    private UUID receiverId;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private UserEntity receiver;

    public PrivateChatRoomEntitiy() {
    }

    public PrivateChatRoomEntitiy(UserEntity _sender, UserEntity _receiver) {
        this.sender = _sender;
        this.receiver = _receiver;
    }

    public PrivateChatRoomEntitiy(UUID id, UserEntity _sender, UserEntity _receiver) {
        this.id = id;
        this.sender = _sender;
        this.receiver = _receiver;
    }

    public void setId(UUID _id) {
        this.id = _id;
    }

    public void setSender(UserEntity _sender) {
        this.sender = _sender;
    }

    public void setReceiver(UserEntity _receiver) {
        this.receiver = _receiver;
    }

    public UUID getId() {
        return id;
    }

    public UserEntity getSender() {
        return sender;
    }

    public UserEntity getReceiver() {
        return receiver;
    }

    public UUID getSenderId() {
        return (sender != null) ? sender.getId() : null;
    }

    public UUID getReceiverId() {
        return (receiver != null) ? receiver.getId() : null;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}
