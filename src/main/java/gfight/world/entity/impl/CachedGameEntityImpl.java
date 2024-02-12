package gfight.world.entity.impl;

import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;

import gfight.common.api.Position2D;
import gfight.engine.graphics.api.GraphicsComponent;
import gfight.world.entity.api.CachedGameEntity;
import gfight.world.entity.api.GameEntity;
import gfight.world.hitbox.api.Hitbox;

/**
 * An abstract implementation of the cached game entity.
 * This class provides caching functionality for game entities to improve
 * performance.
 */
public abstract class CachedGameEntityImpl implements CachedGameEntity {

    private final GameEntity originalEntity;
    private Optional<Hitbox> boundigBox;

    private Optional<Set<GameEntity>> collidedObjects;
    private boolean needResHitbox;
    private boolean needResCollided;

    /**
     * Constructs a CachedGameEntityImpl with the specified vertex positions,
     * position, and graphics component.
     * 
     * @param vertexes          The list of positions defining the vertices.
     * @param position          The position of the center of the entity.
     * @param graphicsComponent The graphics component associated with the entity.
     */
    public CachedGameEntityImpl(final List<Position2D> vertexes, final Position2D position,
            final GraphicsComponent graphicsComponent) {
        originalEntity = new GameEntityImpl(vertexes, position, graphicsComponent);
        this.boundigBox = Optional.empty();
        this.collidedObjects = Optional.empty();
    }

    @Override
    public final Hitbox getHitBox() {
        if (needResHitbox || boundigBox.isEmpty()) {
            needResHitbox = false;
            boundigBox = Optional.of(originalEntity.getHitBox());
        }
        return boundigBox.get();
    }

    /**
     * Resets the state of the entity, indicating that cached data needs to be
     * recalculated.
     */
    @Override
    public void reset() {
        needResHitbox = true;
        needResCollided = true;
    }

    @Override
    public final Set<GameEntity> getAllCollided(final Set<? extends GameEntity> gameObjects) {
        if (needResCollided || collidedObjects.isEmpty()) {
            needResCollided = false;
            collidedObjects = Optional.of(originalEntity.getAllCollided(gameObjects));
        }
        return new LinkedHashSet<>(collidedObjects.get());
    }

    @Override
    public final void setPosition(final Position2D position) {
        originalEntity.setPosition(position);
    }

    @Override
    public final Position2D getPosition() {
        return originalEntity.getPosition();
    }

    @Override
    public final void setIgnoredEntities(final Set<GameEntity> ignoredEntities) {
        originalEntity.setIgnoredEntities(ignoredEntities);
    }

    @Override
    public final Set<GraphicsComponent> getGraphics() {
        return originalEntity.getGraphics();
    }

    @Override
    public final List<Position2D> getPosition2Ds() {
        return originalEntity.getPosition2Ds();
    }

    @Override
    public final void setCoordinates(final List<Position2D> vertexes) {
        originalEntity.setCoordinates(vertexes);
    }

    @Override
    public final void setGraphics(Set<GraphicsComponent> graphics) {
        originalEntity.setGraphics(graphics);
    }
}
