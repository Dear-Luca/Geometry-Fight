package gfight.world.map.impl;

import gfight.common.api.Position2D;
import gfight.world.map.api.Spawner;

/**
 * Implements an abstract Spawner which spawning criteria is not defined.
 */
public abstract class AbstractSpawner implements Spawner {

    private final Position2D position;
    private boolean isEnabled;
    protected int currentLevel;

    /**
     * Creates a new abstract Spawner which spawning criteria is not defined.
     * 
     * @param position the position of the spawner
     */
    public AbstractSpawner(final Position2D position) {
        this.position = position;
        this.currentLevel = 0;
        this.isEnabled = true;
    }

    public void spawn() {
        this.currentLevel++;
    }

    @Override
    public Position2D getPosition() {
        return this.position;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    @Override
    public void enable() {
        this.isEnabled = true;
    }

    @Override
    public void disable() {
        this.isEnabled = false;
    }
}
