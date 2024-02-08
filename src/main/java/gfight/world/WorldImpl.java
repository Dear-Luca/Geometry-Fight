package gfight.world;

import java.util.List;
import java.util.Optional;

import gfight.common.api.Position2D;
import gfight.common.impl.Position2DImpl;
import gfight.engine.graphics.api.GraphicsComponent;
import gfight.engine.graphics.api.MovableCamera;
import gfight.engine.input.api.InputEvent;
import gfight.engine.input.api.InputEventKey;
import gfight.engine.input.api.InputEventMouse;
import gfight.world.api.EntityManager;
import gfight.world.entity.api.Character;
import gfight.world.entity.api.GameEntity;
import gfight.world.entity.api.MovingEntity;
import gfight.world.entity.impl.EntityFactoryImpl;
import gfight.world.hitbox.api.Hitboxes;
import gfight.world.hitbox.impl.HitboxesImpl;
import gfight.world.impl.EntityManagerImpl;
import gfight.world.map.api.GameMap;
import gfight.world.map.impl.GameMapImpl;
import gfight.world.movement.api.InputMovement;
import gfight.world.movement.impl.MovementFactoryImpl;

/**
 * Implementation of a World controlling the execution of the game.
 */
public class WorldImpl implements World {

    private static final int PLAYER_DIM = 30;
    private static final int MAP_DIM = 21;

    private MovableCamera camera;
    private EntityManager entityManager;
    private GameMap map;
    private InputMovement keyMapper;
    private Hitboxes hitboxManager;
    private Character testPlayer;
    private Position2D pointingPosition;

    /**
     * Creates a new instance of a World.
     */
    public WorldImpl() {
        this.entityManager = new EntityManagerImpl(new EntityFactoryImpl());
        this.hitboxManager = new HitboxesImpl();
        this.map = new GameMapImpl(MAP_DIM);
        this.keyMapper = new MovementFactoryImpl().createInput();
        loadMap();
        this.entityManager.createEnemy(testPlayer, 15, new Position2DImpl(50, 250), 20, map);
    }

    @Override
    public final void installCamera(final MovableCamera camera) {
        this.camera = camera;
        this.camera.moveTo(new Position2DImpl(0, 0));
    }

    @Override
    public void initialize() {
    }

    @Override
    public final void update(final long deltaTime) {
        this.hitboxManager.freeHitboxes(this.entityManager.getEntities());
        this.testPlayer.pointTo(this.pointingPosition);
        for (final var entity : this.entityManager.getEntities()) {
            if (entity instanceof MovingEntity) {
                ((MovingEntity) entity).updatePos(deltaTime, this.entityManager.getEntities());
            }
        }
        this.entityManager.clean();
    }

    @Override
    public final List<GraphicsComponent> getGraphicsComponents() {
        return this.entityManager.getEntities().stream().map(GameEntity::getGraphics).toList();
    }

    @Override
    public final void processInput(final InputEvent event) {
        if (event instanceof InputEventKey) {
            manageKey((InputEventKey) event);
        } else if (event instanceof InputEventMouse) {
            managePointer((InputEventMouse) event);
        }
    }

    private void manageKey(final InputEventKey key) {
        final var direction = Optional.ofNullable(switch (key.getKey()) {
            case 87 -> InputMovement.Directions.NORTH;
            case 83 -> InputMovement.Directions.SOUTH;
            case 65 -> InputMovement.Directions.WEST;
            case 68 -> InputMovement.Directions.EAST;
            default -> null;
        });
        if (direction.isPresent()) {
            if (key.getType() == InputEvent.Type.PRESSED) {
                this.keyMapper.addDirection(direction.get());
            }
            if (key.getType() == InputEvent.Type.RELEASED) {
                this.keyMapper.removeDirection(direction.get());
            }
        }
    }

    private void managePointer(final InputEventMouse pointer) {
        this.pointingPosition = pointer.getPosition();
        if (pointer.getType().equals(InputEvent.Type.MOUSE_DOWN)) {
            // add here to make player shoot
        }
    }

    private void loadMap(){
        for(final var pos : this.map.getObstaclesPositions()){
            this.entityManager.createObstacle(GameMap.TILE_DIM, pos);
        }
        this.testPlayer = this.entityManager.createPlayer(PLAYER_DIM, this.map.getPlayerSpawn(), 20, keyMapper);
        this.pointingPosition = new Position2DImpl(this.testPlayer.getPosition().getX(), 0);
    }
}
