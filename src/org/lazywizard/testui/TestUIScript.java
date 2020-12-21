package org.lazywizard.testui;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.campaign.listeners.CampaignInputListener;
import com.fs.starfarer.api.input.InputEventAPI;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class TestUIScript implements EveryFrameScript, CampaignInputListener
{
    private Button testButton = new Button(new Vector2f(0f, 0f), 150f, 40f);

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
}
