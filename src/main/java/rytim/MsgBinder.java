package rytim;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author rtimmons
 * @date 6/20/15
 */
public class MsgBinder {

    public Msg fromObject(final Object o)
        throws IntrospectionException, IllegalAccessException, InvocationTargetException
    {
        ImmutableMap.Builder<String,Object> builder =
            ImmutableMap.builder();


        Map<String,Object> asMap =
        Arrays.stream(Introspector.getBeanInfo(o.getClass())
                                  .getPropertyDescriptors())
              .collect(Collectors.toMap(p -> p.getName(),
                                        p -> extract(o,p.getReadMethod())));
        return new Msg(asMap);
    }

    public Object fromMsg(final Msg msg,Class<?> k)
        throws IntrospectionException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        Object out = k.newInstance();
        Arrays.stream(Introspector.getBeanInfo(k).getPropertyDescriptors())
              .forEach(p -> set(out, p.getWriteMethod(), msg.get(p.getName())));
        return out;
    }


    private void set(Object on, Method writeMethod, Object val)
    {
        try
        {
            writeMethod.invoke(on,val);
        }
        catch (IllegalAccessException|InvocationTargetException e)
        {
            throw Throwables.propagate(e);
        }
    }

    private Object extract(Object o, Method readMethod) {
        try
        {
            return readMethod.invoke(o);
        }
        catch (IllegalAccessException|InvocationTargetException e)
        {
            throw Throwables.propagate(e);
        }
    }
}
