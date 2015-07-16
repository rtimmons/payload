package rytim;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author rtimmons
 * @date 7/16/15
 */
class AbstractImmutable
{
    static Object transform(Object o)
    {
        o = Preconditions.checkNotNull(o);
        // TODO: marker interface?
        if ( o instanceof Msg ||
             o instanceof MList )
        {
            return o;
        }
        if ( o instanceof Map ) { return new Msg((Map)o);    }
        if ( o instanceof List) { return new MList((List)o); }
        return o;
    }

    static boolean accepts(Object value)
    {
        return accepts("?", value);
    }
    static boolean accepts(String key, Object value)
    {
        return value != null && (
               value instanceof String  ||
               value instanceof Number  ||
               value instanceof Boolean ||
               value instanceof Date    ||
               value instanceof Msg     ||
               value instanceof MList   ||
               value instanceof Collection && acceptsCollection(key, value) ||
               value instanceof Map        && acceptsMap(key, value)
        );

    }

    static boolean acceptsMap(Object value)
    {
        return acceptsMap("?",value);
    }

    static boolean acceptsMap(String key, Object value)
    {
        Preconditions.checkArgument(value instanceof Map,
                                    "Object at key %s must be map: %s",
                                    key,
                                    value);
        return ((Map<String,Object>)value)
            .entrySet()
            .stream()
            .allMatch(e -> accepts(e.getKey(), e.getValue()));
    }

    static
    boolean acceptsCollection(final Object value)
    {
        return acceptsCollection("?", value);
    }

    static
    boolean acceptsCollection(final String key,
                              final Object value)
    {
        Preconditions.checkArgument(value instanceof Collection,
                                    "Object at key %s must be Collection: %s",
                                    key,
                                    value);
        return value instanceof ImmutableCollection &&
            ((Collection<?>) value)
                .stream()
                .allMatch(i -> accepts(key, i));
    }
}
