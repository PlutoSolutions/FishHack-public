package cat.cattyn.fishhack.client.modules.render;

import cat.cattyn.fishhack.api.event.events.FogEvent;
import cat.cattyn.fishhack.client.modules.Module;
import com.google.common.eventbus.Subscribe;

@Module.Info(name = "AntiFog", cat = Module.Category.Render)
public class AntiFog extends Module {
    @Subscribe
    public void onFog(FogEvent event) {
        event.cancel();
    }
}
