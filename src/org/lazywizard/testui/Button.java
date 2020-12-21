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
    protected float sizeX, sizeY;
    protected State state = State.UNPRESSED;

    public enum State
    {
        UNPRESSED,
        MOUSED_OVER,
        PRESSED
    }

    public Button(float x, float y, float sizeX, float sizeY)
    {
        buttonDown = Global.getSettings().getSprite(BUTTON_DOWN);
        buttonUp = Global.getSettings().getSprite(BUTTON_UP);
        buttonOver = Global.getSettings().getSprite(BUTTON_OVER);

        this.position = new Vector2f(x, y);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public Button(Vector2f position, float sizeX, float sizeY)
    {
        this(position.x, position.y, sizeX, sizeY);
    }

    //<editor-fold desc="Getters and setters" defaultstate="collapsed">
    public Vector2f getPosition()
    {
        return position;
    }

    public float getSizeX()
    {
        return sizeX;
    }

    public void setSizeX(float sizeX)
    {
        this.sizeX = sizeX;
    }

    public float getSizeY()
    {
        return sizeY;
    }

    public void setSizeY(float sizeY)
    {
        this.sizeY = sizeY;
    }
    //</editor-fold>

    /**
     * Called on the first frame a button is pressed.
     */
    protected abstract void onButtonPressed();

    /**
     * Called on the frame the mouse is released after the button has been pressed.
     */
    protected abstract void onButtonReleased();

    /**
     * Test if a point is within the button's confines. Used internally to detect clicks and mouseovers, but may have
     * other uses.
     *
     * @param x The x coordinate of the point to check.
     * @param y The y coordinate of the point to check.
     *
     * @return {@code true} if the point is within the button, @code false} otherwise.
     */
    public boolean isInButton(float x, float y)
    {
        return (x >= position.x && x <= position.x + sizeX &&
                y >= position.y && y <= position.y + sizeY);
    }

    /**
     * Test if a point is within the button's confines.
     *
     * @see #isInButton(float, float)
     */
    public boolean isInButton(Vector2f toCheck)
    {
        return isInButton(toCheck.x, toCheck.y);
    }

    /**
     * Processes mouse and keyboard input. Make sure to call {@code super.processInput(events)} if you overwrite this!
     *
     * @param events All input events this frame.
     */
    // TODO: add right/middle mouse click handling
    protected void processInput(List<InputEventAPI> events)
    {
        final boolean inButton = isInButton(Mouse.getX(), Mouse.getY());

        for (InputEventAPI event : events)
        {
            // We're only interested in mouse events involving the left mouse button
            if (event.isConsumed() || !event.isMouseEvent() || event.getEventValue() != 0) continue;

            // Button clicked
            if (event.isMouseDownEvent() && inButton)
            {
                state = State.PRESSED;
                onButtonPressed();
                event.consume();
            }
            // Button released
            else if (event.isMouseUpEvent() && state == State.PRESSED)
            {
                state = State.UNPRESSED;
                onButtonReleased();
                event.consume();
            }
        }

        // If the button isn't being clicked, check mouseover state for highlighting
        if (state != State.PRESSED)
        {
            state = (inButton ? State.MOUSED_OVER : State.UNPRESSED);
        }
    }

    /**
     * Useful for custom animated effects.
     */
    protected void advance(float amount)
    {
    }

    /**
     * Draws the button. This method currently handles setting up the viewport and draw flags, but that will likely
     * change in the future as it's very inefficient with many buttons.
     */
    protected void render()
    {
        final int width = (int) (Display.getWidth() * Display.getPixelScaleFactor()),
                height = (int) (Display.getHeight() * Display.getPixelScaleFactor());
        final SpriteAPI activeSprite;
        switch (state)
        {
            case UNPRESSED:
                activeSprite = buttonUp;
                break;
            case MOUSED_OVER:
                activeSprite = buttonOver;
                break;
            case PRESSED:
                activeSprite = buttonDown;
                break;
            default:
                throw new RuntimeException("Unhandled button state: " + state.name());
        }

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
