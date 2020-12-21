package org.lazywizard.testui;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;

public class TestUIModPlugin extends BaseModPlugin
{
    @Override
    public void onApplicationLoad() throws Exception
    {
        Global.getSettings().loadTexture(Button.BUTTON_UP);
        Global.getSettings().loadTexture(Button.BUTTON_DOWN);
        Global.getSettings().loadTexture(Button.BUTTON_OVER);
    }

    @Override
    public void onGameLoad(boolean newGame)
    {
        final TestUIScript script = new TestUIScript();
        Global.getSector().addTransientScript(script);
        Global.getSector().getListenerManager().addListener(script, true);
    }
}
