package rytim

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import org.junit.Test
import rytim.Msg

/**
 * @author rtimmons
 * @date 6/20/15
 */
class EventBusSandbox
{

    @Test
    public void testEventBus()
    {
        EventBus bus = new EventBus();
        LoggingListener listener = new LoggingListener()
        bus.register(listener);

        bus.post(new Msg("name","Ryan"));
    }

    class LoggingListener
    {
        @Subscribe public void onMessage(Msg msg)
        {
            println msg;
        }
    }
}
