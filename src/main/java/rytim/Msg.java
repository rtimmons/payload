package rytim;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.json.simple.JSONObject;

import java.util.Map;
import java.util.Optional;


/**
 * @author rtimmons
 * @date 6/20/15
 */
public class Msg
    extends AbstractImmutable
{
    private final ImmutableMap<String,Object> delegate;

    public Msg(Object...entries)
    {
        Preconditions.checkArgument(entries != null &&
                                    entries.length % 2 == 0,
                                    "Need even number of args given %s in [%s]",
                                    entries.length, entries);
        ImmutableMap.Builder<String,Object> b = ImmutableMap.builder();
        for(int i=0; i < entries.length; i+=2)
        {
            Object k = Preconditions.checkNotNull(entries[i],   entries);
            Object v = Preconditions.checkNotNull(entries[i+1], entries);
            Preconditions.checkArgument(k instanceof String,
                                        "Key %s must be string", k);

            String key = (String)k;
            Preconditions.checkArgument(accepts(key,v),
                                        "Doesn't accept key %s: %s", key, v);

            b.put(key, v);
        }
        this.delegate = b.build();
    }

    public Msg(ImmutableMap<String,Object> vals)
    {
        Preconditions.checkArgument(accepts("?",vals),"Object type matters - given %s", vals);
        this.delegate = vals;
    }

    public Msg(Map<String, Object> source)
    {
        this(ImmutableMap.copyOf(source));
    }

    public Msg with(String key, Object value)
    {
        Preconditions.checkNotNull(key, "Attempt to use null key for value=%s", value);
        Preconditions.checkNotNull(value, "Attempt to put null at key %s", key);
        Preconditions.checkArgument(accepts(key,value),
                                    "Type matters %s: %s", key, value);

        ImmutableMap.Builder<String,Object> builder =
            ImmutableMap.builder();

        builder.put(key,value);

        delegate.forEach((k,v)->{
            if ( !key.equals(k) )
            {
                builder.put(k,v);
            }
        });
        return new Msg(builder.build());
    }

    public Msg without(final String key)
    {
        Preconditions.checkNotNull(key);
        ImmutableMap.Builder b =
            ImmutableMap.<String,Object>builder();

        delegate.forEach((k,v) -> {
            if ( !key.equals(k) )
                b.put(k,v);
        });

        return new Msg(b.build());
    }

    public Optional<Object> get(String key)
    {
        return Optional.ofNullable(delegate.get(key));
    }

    public Msg transformEntries(Maps.EntryTransformer<String,Object,Object> xform)
    {
        return new Msg(ImmutableMap.copyOf(Maps.transformEntries(delegate,xform)));
    }

    public String toString()
    {
        return JSONObject.toJSONString(delegate);
    }

    @Override public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Msg))
        {
            return false;
        }

        Msg msg = (Msg) o;

        return delegate.equals(msg.delegate);

    }

    @Override public int hashCode()
    {
        return delegate.hashCode();
    }
}
