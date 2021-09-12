package cat.cattyn.fishhack.client.modules.render;

import cat.cattyn.fishhack.api.event.events.RenderEvent;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;
import cat.cattyn.fishhack.api.util.Renderer;
import com.google.common.eventbus.Subscribe;

import java.util.Arrays;

@Module.Info(name = "TestModule", desc = "Testin", cat = Module.Category.Render)
public class TestModule extends Module {
    Setting<Boolean> testBool = register("Test", false);
    Setting<Double> testInt = register("Test", 1, 0, 10, 0);
    Setting<Double> testDouble = register("Test", 1f, 0, 10, 2);
    Setting<String> testMode = register("Test", Arrays.asList("1", "2", "3"), "1");

    @Subscribe
    public void onRender(RenderEvent event) {

        // shadow
        //FishHack.fontRender.drawString("Fish Shit Hack", 10.5, 110.5, ColorUtil.BLACK );
        //FishHack.fontRender.drawString("Fish Shit Hack", 10, 110, ColorUtil.RED );

        // outline
		/*
		// right up
		FishHack.fontRender.drawString("Fish Shit Hack", 10.5, 109.5, ColorUtil.BLACK );
		// right down
		FishHack.fontRender.drawString("Fish Shit Hack", 10.5, 110.5, ColorUtil.BLACK );
		// left down
		FishHack.fontRender.drawString("Fish Shit Hack", 9.5, 110.5, ColorUtil.BLACK );
		// left up
		FishHack.fontRender.drawString("Fish Shit Hack", 9.5, 109.5, ColorUtil.BLACK );
		
		FishHack.fontRender.drawString("Fish Shit Hack", 10, 110, ColorUtil.WHITE );
		*/

        Renderer.INSTANCE.setup2DRender(true);

        //Renderer.INSTANCE.fill(event.getMatrixStack(), 5, 5, 50, 50,
        //		new java.awt.Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());

        Renderer.INSTANCE.end2DRender();
    }
}
