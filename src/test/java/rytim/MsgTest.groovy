package rytim

import org.junit.Test
/**
 * @author rtimmons
 * @date 7/16/15
 */
class MsgTest
{

    // convenience
    def a = 'a',
        b = 'b',
        c = 'c',
        date = new Date(2015,7,16),
        cray = new Object() {
            @Override public String toString(){ "Some Crazy Object" }
        };
    ;

    Msg ab = new Msg(a,a,
                     b,b)

    def withAB(Closure c,Class<? extends Throwable> t = null)
    {
        try
        {
            def out = c.call(ab)
            if ( t != null )
            {
                assert false, "Should have thrown $t instead returned $out"
            }
        }
        catch(Exception x)
        {
            assert x.getClass().isAssignableFrom(t),
                   "Unexpected type $x expected $t"
        }
    }

    @Test
    public void testCreateAndModify()
    {
        assert a == ab.get(a).get()
        assert b == ab.get(b).get()
        assert !ab.get(c).isPresent()

        def withC = ab.with(c,c)
        assert a == withC.get(a).get()
        assert b == withC.get(b).get()
        assert c == withC.get(c).get()
        assert !ab.get(c).isPresent()
        assert withC.get(c).isPresent()

        def withoutC = withC.without(c)
        assert withC.get(c).isPresent()
        assert !withoutC.get(c).isPresent()

        assert ab == withoutC
    }

    @Test
    public void testChangeValue()
    {
        assert ab.with(a,date).get(a).get() == date
    }

    @Test
    public void testTypes()
    {
        Msg m, m2

        m = new Msg(a,date)
        assert m.get(a).get() == date

        m = new Msg(a,true)
        assert m.get(a).get()

        m2 = m.with(a,false)
        assert !m2.get(a).get()
    }

    def mustThrow(List args,String type='arg')
    {
        try
        {
            println new Msg(args.toArray())
            assert false, "must throw on [$args]"
        }
        catch(Exception x)
        {
            switch(type) {
                case 'arg':
                    assert x instanceof IllegalArgumentException,
                           x.stackTrace.join("\n")
                    break
                case 'npe':
                    assert x instanceof NullPointerException,
                           x.stackTrace.join("\n")
                    break
                default:
                    assert false,
                        "Unexpected exception $x thrown for [$args] " +
                        x.stackTrace.join("\n")
            }
        }

    }

    @Test
    public void testThrows()
    {
        // odd arity
        mustThrow([a])
        mustThrow([null])
        mustThrow([0])

        // odd arity
        mustThrow([a,a,a])
        mustThrow([null,null,null])
        mustThrow([0,0,0])

        // null values
        mustThrow([a,null],         'npe')
        mustThrow([null,null],      'npe')
        mustThrow([0,null],         'npe')
        mustThrow([a,a,b,null],     'npe')

        // not-acceptable
        mustThrow([a,cray])
        mustThrow([cray,a])
        mustThrow([a,a,b,cray])


        // can't overwrite in convenience constructor
        mustThrow([a,a, a,b])
    }

    @Test
    public void testInvalidWith()
    {
        withAB({it.with(null,a)}, NullPointerException)
        withAB({it.with(null,a)}, NullPointerException)
        withAB({it.with(a,cray)}, IllegalArgumentException)
    }

    @Test
    public void testConstructsMList()
    {
        def ml = new Msg(a, [1,2,3])
        def l = ml.get(a).get()
        assert l instanceof MList
        assert l.get(0) == 1
    }

    @Test
    public void testConstructsChildMsg()
    {
        def ml = new Msg(a, new Msg(a,b))
        def l = ml.get(a).get()
        assert l instanceof Msg
        assert l.get(a).get() == b
    }
}
