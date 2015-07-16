package rytim;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static rytim.AbstractImmutable.accepts;


/**
 * Delegate class for immutable lists
 *
 * @author rtimmons
 * @date 7/16/15
 */
public class MList
{
    final ImmutableList delegate;

    public MList(ImmutableList<Object> vals)
    {
        vals = Preconditions.checkNotNull(vals);
        Preconditions.checkArgument(
            vals.stream().allMatch(AbstractImmutable::accepts)
        );
        this.delegate =
            ImmutableList.copyOf((Collection<?>) // ick?
                vals.stream()
                    .map(AbstractImmutable::transform)
                    .collect(Collectors.toCollection(Lists::newLinkedList))
            );
    }

    public MList(List<Object> vals)
    {
        this(ImmutableList.copyOf(vals));
    }

    public MList push(Object val)
    {
        val = Preconditions.checkNotNull(val);
        Preconditions.checkArgument(accepts(val));

        // TODO: this double-checks values - since
        // they're in `this` they're already valid
        // and `val` is valid by above check
        // but the constructor checks all entries anyway
        return new MList(ImmutableList.builder()
                                      .addAll(delegate)
                                      .add(val)
                                      .build());
    }

    public Object get(int index)
    {
        return delegate.get(index);
    }

    public int size()
    {
        return delegate.size();
    }

    @Override public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof MList))
        {
            return false;
        }

        MList mList = (MList) o;

        return delegate.equals(mList.delegate);

    }

    @Override public int hashCode()
    {
        return delegate.hashCode();
    }
}
