package com.E3N.pix.domain;

import java.time.Instant;
import java.util.UUID;

public abstract class Entity {
    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant deletedAt;

    public Entity(UUID id, Instant createdAt, Instant updatedAt, Instant deletedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
}
