package gfight.world.entity.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import gfight.common.api.Position2D;
import gfight.common.impl.Position2DImpl;
import gfight.engine.graphics.api.EngineColor;
import gfight.engine.graphics.api.GraphicsComponent;
import gfight.engine.graphics.api.StatusBarGraphicsComponent;
import gfight.engine.graphics.api.GraphicsComponent.GraphicType;
import gfight.engine.graphics.impl.GraphicsComponentsFactoryImpl;
import gfight.world.entity.api.ActiveEntity;
import gfight.world.entity.api.GameEntity;

/**
 * This class implements the concept of ActiveEntity with movement and health
 * (Chest, Player and Enemies).
 */
public abstract class AbstractActiveEntity extends BaseMovingEntity implements ActiveEntity {
    private static final int HEALTH_BAR_WIDTH = 25;
    private static final int HEALTH_BAR_HEIGHT = 4;
    private static final int HEALTH_BAR_OFFSET = -25;

    private int health;

    /**
     * Constructor of ActiveEntityImpl.
     * 
     * @param vertexes          of the shape
     * @param position          the center of the shape
     * @param graphicsComponent the color of the shape
     * @param health            of the entity
     */
    public AbstractActiveEntity(final List<Position2D> vertexes, final Position2D position,
            final GraphicsComponent graphicsComponent, final int health) {
        super(vertexes, position, graphicsComponent);
        this.health = health;
        final StatusBarGraphicsComponent healthBar = new GraphicsComponentsFactoryImpl().statusBar(
                EngineColor.RED,
                EngineColor.GREEN,
                position,
                HEALTH_BAR_WIDTH,
                HEALTH_BAR_HEIGHT,
                GraphicType.WORLD);
        healthBar.setRange(0, getHealth());
        healthBar.setStatus(getHealth());
        setGraphics(Stream.concat(Stream.of(healthBar), super.getGraphics().stream()).collect(Collectors.toSet()));
    }

    @Override
    public final int getHealth() {
        return this.health;
    }

    @Override
    public final void takeDamage(final int damage) {
        this.setHealth(getHealth() - damage);
        getGraphics().stream().filter(el -> el instanceof StatusBarGraphicsComponent).forEach(bar -> {
            final StatusBarGraphicsComponent statusBar = (StatusBarGraphicsComponent) bar;
            statusBar.setStatus(getHealth());
        });
    }

    /**
     * This method can be overridden.
     */
    @Override
    public boolean isAlive() {
        return this.getHealth() > 0;
    }

    /**
     * Set the life point of the entity.
     * 
     * @param health the actual health of the entity
     */
    protected void setHealth(final int health) {
        this.health = health;
    }

    @Override
    public final void updatePos(final long dt, final Set<? extends GameEntity> gameobjects) {
        super.updatePos(dt, gameobjects);
        getGraphics().stream().filter(el -> el instanceof StatusBarGraphicsComponent).forEach(bar -> {
            final StatusBarGraphicsComponent statusBar = (StatusBarGraphicsComponent) bar;
            statusBar.setPositions(List.of(new Position2DImpl(getPosition().getX(), getPosition().getY() + HEALTH_BAR_OFFSET)));
        });
    }
}
