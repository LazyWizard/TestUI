package org.lazywizard.testui;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Button
{
    public static final String BUTTON_UP = "graphics/ui/buttons/button_up.png";
    public static final String BUTTON_DOWN = "graphics/ui/buttons/button_down.png";
    public static final String BUTTON_OVER = "graphics/ui/buttons/button_over.png";
    private final SpriteAPI buttonDown, buttonUp, buttonOver;
    private SpriteAPI activeSprite;
    private Vector2f position;
    private float sizeX, sizeY;
    private boolean isClicked, isMousedOver;

    public Button(Vector2f position, float sizeX, float sizeY)
    {
        buttonDown = Global.getSettings().getSprite(BUTTON_DOWN);
        buttonUp = Global.getSettings().getSprite(BUTTON_UP);
        buttonOver = Global.getSettings().getSprite(BUTTON_OVER);

        this.position = position;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        isClicked = false;
        isMousedOver = false;
        activeSprite = buttonUp;
    }

    public Vector2f getPosition()
    {
        return position;
    }

    public boolean isInButton(Vector2f toCheck)
    {
        return (toCheck.x >= position.x && toCheck.x <= position.x + sizeX &&
                toCheck.y >= position.y && toCheck.y <= position.y + sizeY);
    }

    // TODO: add onDown, onUp, onMouseover etc methods
    public void processInput(List<InputEventAPI> events)
    {
        final Vector2f mousePos = new Vector2f(Mouse.getX(), Mouse.getY());
        final boolean inButton = isInButton(mousePos);

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        {
            position.set(mousePos);
        }

        for (InputEventAPI event : events)
        {
            if (event.isConsumed()) continue;

            if (event.isMouseDownEvent() && inButton)
            {
                activeSprite = buttonDown;
                event.consume();
            }
            else if (event.isMouseUpEvent() && activeSprite == buttonDown)
            {
                activeSprite = buttonUp;
                event.consume();
            }
        }

        if (!inButton || activeSprite != buttonDown)
        {
            activeSprite = (inButton ? buttonOver : buttonUp);
        }
    }

    public void advance(float amount)
    {
    }

    public void render()
    {
        glPushAttrib(GL_ALL_ATTRIB_BITS);
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        int width = (int) (Display.getWidth() * Display.getPixelScaleFactor()),
                height = (int) (Display.getHeight() * Display.getPixelScaleFactor());
        glViewport(0, 0, width, height);
        glOrtho(0, width, 0, height, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glTranslatef(0.01f, 0.01f, 0);

        activeSprite.setSize(sizeX, sizeY);
        activeSprite.render(position.x, position.y);

        // Finalize drawing
        glDisable(GL_BLEND);
        glPopMatrix();
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glPopAttrib();
    }
}
