package rytim;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;


/**
 * @author rtimmons
 * @date 6/20/15
 */
public class Dispatcher
{
    private final ImmutableMap<String,EventBus> topics;

    public void submit(String topic, Msg msg)
    {
        topics.get(topic).post(msg);
    }

    public Dispatcher(Map<String,List<Object>> listeners)
    {
        topics = ImmutableMap.<String,EventBus>copyOf(
        Maps.transformValues(listeners, ls -> {
            EventBus out = new EventBus(); // TODO: wire in topic name to constructor
            ls.stream().forEach(out::register);
            ls.stream().forEach(this::registerSelf);
            return out;
        }));
    }

    @FunctionalInterface
    private interface ThrowableFunction<F,T> extends Function<F,T> {
        @Override
        default T apply(F o)
        {
            try
            {
                System.out.println("Throwable of " + o);
                return throwyApply(o);
            }
            catch(Exception x)
            {
                x.printStackTrace(System.err);
                throw Throwables.propagate(x);
            }
        }
        T throwyApply(F arg) throws Exception;
    }

    private <F,T> Function<F,T> quietly(ThrowableFunction<F,T> f)
    {
        return f::apply;
    }

    public <A, B, C> Function<A, Function<B, C>>
        curry(final BiFunction<A, B, C> f)
    {
        return (A a) -> (B b) -> f.apply(a, b);
    }

    private void registerSelf(Object o)
    {
        // TODO
//        Preconditions.checkNotNull(o);
//        Arrays.asList(o.getClass().getMethods())
//              .stream()
//              .filter((m) ->
//                          m.isAnnotationPresent(DispatcherAware.class) &&
//                          m.getParameterCount() == 1 &&
//                          Dispatcher.class.isAssignableFrom(m.getParameterTypes()[0])
//            )
//            .forEach(method -> quietly(curry(method::invoke)));
    }
}
