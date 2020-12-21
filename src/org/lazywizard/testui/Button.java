package org.lazywizard.testui;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public abstract class Button
{
    public static final String BUTTON_UP = "graphics/ui/buttons/button_up.png";
    public static final String BUTTON_DOWN = "graphics/ui/buttons/button_down.png";
    public static final String BUTTON_OVER = "graphics/ui/buttons/button_over.png";
    protected final SpriteAPI buttonDown, buttonUp, buttonOver;
    protected final Vector2f position;
    protected SpriteAPI activeSprite; // This also acts as our state check, which... isn't optimal
    protected float sizeX, sizeY;

    public Button(Vector2f position, float sizeX, float sizeY)
    {
        buttonDown = Global.getSettings().getSprite(BUTTON_DOWN);
        buttonUp = Global.getSettings().getSprite(BUTTON_UP);
        buttonOver = Global.getSettings().getSprite(BUTTON_OVER);

        this.position = position;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        activeSprite = buttonUp;
    }

    public abstract void onButtonPressed();

    public abstract void onButtonReleased();

    public Vector2f getPosition()
    {
        return position;
    }

    public boolean isInButton(Vector2f toCheck)
    {
        return (toCheck.x >= position.x && toCheck.x <= position.x + sizeX &&
                toCheck.y >= position.y && toCheck.y <= position.y + sizeY);
    }

    // TODO: add right/middle mouse click handling
    public void processInput(List<InputEventAPI> events)
    {
        final Vector2f mousePos = new Vector2f(Mouse.getX(), Mouse.getY());
        final boolean inButton = isInButton(mousePos);

        for (InputEventAPI event : events)
        {
            // We're only interested in mouse events involving the left mouse button
            if (event.isConsumed() || !event.isMouseEvent() || event.getEventValue() != 0) continue;

            // Button clicked
            if (event.isMouseDownEvent() && inButton)
            {
                activeSprite = buttonDown;
                onButtonPressed();
                event.consume();
            }
            // Button released
            else if (event.isMouseUpEvent() && activeSprite == buttonDown)
            {
                activeSprite = buttonUp;
                onButtonReleased();
                event.consume();
            }
        }

        // If the button isn't being clicked, check mouseover state for highlighting
        // TODO: Use a proper state variable
        if (activeSprite != buttonDown)
        {
            activeSprite = (inButton ? buttonOver : buttonUp);
        }
    }

    public void advance(float amount)
    {
    }

    public void render()
    {
        final int width = (int) (Display.getWidth() * Display.getPixelScaleFactor()),
                height = (int) (Display.getHeight() * Display.getPixelScaleFactor());

        glPushAttrib(GL_ALL_ATTRIB_BITS);
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glViewport(0, 0, width, height);
        glOrtho(0, width, 0, height, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glTranslatef(0.01f, 0.01f, 0);

        activeSprite.setSize(sizeX, sizeY);
        activeSprite.render(position.x, position.y);

        glDisable(GL_BLEND);
        glPopMatrix();
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glPopAttrib();
    }
}
