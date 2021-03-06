package org.lazywizard.testui;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.campaign.listeners.CampaignInputListener;
import com.fs.starfarer.api.input.InputEventAPI;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.List;

public class TestUIScript implements EveryFrameScript, CampaignInputListener
{
    private final Button testButton = new TestButton(0f, 0f, 150f, 40f);

    @Override
    public boolean isDone()
    {
        return false;
    }

    @Override
    public boolean runWhilePaused()
    {
        return true;
    }

    @Override
    public int getListenerInputPriority()
    {
        return 100;
    }

    @Override
    public void processCampaignInputPreCore(List<InputEventAPI> events)
    {
        testButton.processInput(events);
    }

    @Override
    public void processCampaignInputPreFleetControl(List<InputEventAPI> events)
    {
    }

    @Override
    public void processCampaignInputPostCore(List<InputEventAPI> events)
    {
    }

    @Override
    public void advance(float amount)
    {
        testButton.advance(amount);
        testButton.render();
    }

    private static class TestButton extends Button
    {
        private TestButton(float x, float y, float sizeX, float sizeY)
        {
            super(x, y, sizeX, sizeY);
        }

        public void advance(float amount)
        {
            super.advance(amount);

            // Holding left shift lets you reposition the button
            // This is here for initial debugging/verifying of event consumption
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            {
                position.set(Mouse.getX(), Mouse.getY());
            }
        }

        public void onButtonPressed()
        {
            System.out.println("Button pressed.");
        }

        public void onButtonReleased()
        {
            System.out.println("Button released.");
        }
    }
}
