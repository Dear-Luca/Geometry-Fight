package gfight.world.collision.impl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gfight.world.collision.api.CollisionCommand;
import gfight.world.entity.api.GameEntity;
import gfight.world.entity.api.MovingEntity;

/**
 * Abstract class that implements Collision Command.
 * 
 * @param <M> is the entity that moves and causes the collision
 * @param <G> is the other entity
 */
@SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "This class uses the actual collider and collided")
public abstract class AbstractCollisionCommand<M extends MovingEntity, G extends GameEntity>
        implements CollisionCommand<M, G> {
    private final M collider;
    private final G collided;

    /**
     * 
     * @param collider the moving entity that collides.
     * @param collided the other entity.
     */
    public AbstractCollisionCommand(final M collider, final G collided) {
        this.collided = collided;
        this.collider = collider;
    }

    /**
     * 
     * @return the collider
     */
    protected M collider() {
        return collider;
    }

    /**
     * 
     * @return the collided
     */
    protected G collided() {
        return collided;
    }
}
