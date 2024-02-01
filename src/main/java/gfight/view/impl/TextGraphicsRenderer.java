package gfight.view.impl;

import java.awt.Font;
import java.awt.Graphics2D;

import gfight.common.Position2D;
import gfight.engine.graphics.api.GraphicsComponent;
import gfight.engine.graphics.api.ViewableCamera;
import gfight.engine.graphics.impl.TextGraphicsComponent;

/**
 * The renderer for TextGraphicsComponent.
 */
public final class TextGraphicsRenderer extends AbstractGraphicsComponentRenderer {

    @Override
    boolean isCompValid(final GraphicsComponent gComp) {
        return gComp instanceof TextGraphicsComponent;
    }

    @Override
    void renderComp(final Graphics2D g, final ViewableCamera camera) {
        final TextGraphicsComponent gComp = (TextGraphicsComponent) getGraphicsComponent();
        final Position2D printPos = camera.getRelativePosition(gComp.getPositions().get(0));

        g.setFont(new Font("Verdana", Font.PLAIN, gComp.getSize()));
        g.drawString(gComp.getText(), printPos.getX(), printPos.getY());
    }

}