package rytim;

import com.google.common.eventbus.Subscribe;

import java.util.Date;

/**
 * @author rtimmons
 * @date 6/20/15
 */
public class LoggingListener {

    @Subscribe
    public void onMessage(Msg msg)
    {
        System.out.println("Logging Listener " + new Date());
        System.out.println(msg);
    }
}
