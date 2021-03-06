package rytim;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.json.simple.JSONObject;

import java.util.Optional;

import static rytim.AbstractImmutable.accepts;
import static rytim.AbstractImmutable.transform;


/**
 * @author rtimmons
 * @date 6/20/15
 */
public class Msg // TODO: make this implement Map?
{
    private final ImmutableMap<String,Object> delegate;

    public Msg(Object...entries)
    {
        entries = Preconditions.checkNotNull(entries);
        Preconditions.checkArgument(entries.length % 2 == 0,
                                    "Need even number of args given %s in [%s]",
                                    entries.length, entries);
        ImmutableMap.Builder<String,Object> b = ImmutableMap.builder();
        for(int i=0; i < entries.length; i+=2)
        {
            // TODO: move to AbstractImmutable
            Object k = Preconditions.checkNotNull(entries[i],   entries);
            Object v = Preconditions.checkNotNull(entries[i+1], entries);
            Preconditions.checkArgument(k instanceof String,
                                        "Key %s must be string", k);
            String key = (String)k;

            v = transform(v); // TODO: this is getting messy...
            Preconditions.checkArgument(accepts(key,v),
                                        "Doesn't accept key %s: %s", key, v);
            b.put(key, v);
        }
        this.delegate = b.build();
    }

    // TODO: expose public constructor: make this private and
    //       add boolean param to indicate if should check
    private Msg(ImmutableMap<String,Object> vals)
    {
        this.delegate = vals;
    }

    // TODO: this isn't right..need to xform values (recurse)
//    public Msg(Map<String, Object> source)
//    {
//        this(ImmutableMap.copyOf(source));
//    }

    public Msg with(String key, Object value)
    {
        Preconditions.checkNotNull(key, "Attempt to use null key for value=%s", value);
        Preconditions.checkNotNull(value, "Attempt to put null at key %s", key);
        Preconditions.checkArgument(accepts(key,value),
                                    "Type matters %s: %s", key, value);

        ImmutableMap.Builder<String,Object> builder =
            ImmutableMap.builder();

        builder.put(key,transform(value));

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
