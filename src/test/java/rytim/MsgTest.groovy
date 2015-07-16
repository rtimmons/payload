package rytim

import org.junit.Test

/**
 * @author rtimmons
 * @date 7/16/15
 */
class MsgTest
{
    def msg(Map msg) { return new Msg(msg) }

    // convenience
    def a = 'a',
        b = 'b',
        c = 'c'
    ;

    Msg ab = msg(a:a,b:b)

    @Test
    public void testCreateMsg()
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
}
