package rytim
import com.google.common.eventbus.Subscribe
import org.junit.Test
/**
 * @author rtimmons
 * @date 6/20/15
 */
class DispatcherTest {

    @Test
    public void testWiring()
    {
        Dispatcher d = new Dispatcher([
                "log": [new PrintingListener(prefix: 1),
                        new PrintingListener(prefix: 2),]
        ])
        d.submit("log", new Msg("k","v"))
    }

    class PrintingListener {
        def prefix
        @Subscribe public void onMsg(Msg m)
        {
            println "#$prefix: $m";
        }
        @DispatcherAware public void setDispatcher(Dispatcher d)
        {
            println "#$prefix: Set dispatcher to $d"
        }
    }
}
