/**
 * Test suite to demonstrate a good method for comparing floating-point values
 * using an epsilon. Run via JUnit 4.
 *
 * Note: this function attempts a "one size fits all" solution. There may be
 * some edge cases for which it still produces unexpected results, and some of
 * the tests it was developed to pass probably specify behaviour that is not
 * appropriate for some applications, especially concerning very small values
 * with differing signs.
 *
 * Before using it, make sure it's appropriate for your application!
 *
 * From http://floating-point-gui.de
 *
 * @author Michael Borgwardt
 */
public class NearlyEqualsTest {
    public static void main(String[] args) {
        big();
        bigNeg();
        mid();
        midNeg();
        small();
        smallNeg();
        smallDiffs();
        zero();
        extremeMax();
        infinities();
        nan();
        opposite();
        ulp();
    }
    
    public static boolean nearlyEqual(float a, float b, float epsilon) {
        final float absA = Math.abs(a);
        final float absB = Math.abs(b);
        final float diff = Math.abs(a - b);

        if (a == b) { // shortcut, handles infinities
            return true;
        } else if (a == 0 || b == 0 || (absA + absB < Float.MIN_NORMAL)) {
            // a or b is zero or both are extremely close to it
            // relative error is less meaningful here
            return diff < (epsilon * Float.MIN_NORMAL);
        } else { // use relative error
            return diff / Math.min((absA + absB), Float.MAX_VALUE) < epsilon;
        }
    }

    public static boolean nearlyEqual(float a, float b) {
        return nearlyEqual(a, b, 0.00001f);
    }

    public static void assertTrue(boolean val)
    {
        System.out.println("assertTrue: "+val);
    }

    public static void assertFalse(boolean val)
    {
        System.out.println("assertFalse: "+val);
    }

    /** Regular large numbers - generally not problematic */
    public static void big() {
        System.out.println("---- big ----");
        assertTrue(nearlyEqual(1000000f, 1000001f));
        assertTrue(nearlyEqual(1000001f, 1000000f));
        assertFalse(nearlyEqual(10000f, 10001f));
        assertFalse(nearlyEqual(10001f, 10000f));
    }

    /** Negative large numbers */
    public static void bigNeg() {
        System.out.println("---- bigNeg ----");
        assertTrue(nearlyEqual(-1000000f, -1000001f));
        assertTrue(nearlyEqual(-1000001f, -1000000f));
        assertFalse(nearlyEqual(-10000f, -10001f));
        assertFalse(nearlyEqual(-10001f, -10000f));
    }

    /** Numbers around 1 */
    public static void mid() {
        System.out.println("---- mid ----");
        assertTrue(nearlyEqual(1.0000001f, 1.0000002f));
        assertTrue(nearlyEqual(1.0000002f, 1.0000001f));
        assertFalse(nearlyEqual(1.0002f, 1.0001f));
        assertFalse(nearlyEqual(1.0001f, 1.0002f));
    }

    /** Numbers around -1 */
    public static void midNeg() {
        System.out.println("---- midNeg ----");
        assertTrue(nearlyEqual(-1.000001f, -1.000002f));
        assertTrue(nearlyEqual(-1.000002f, -1.000001f));
        assertFalse(nearlyEqual(-1.0001f, -1.0002f));
        assertFalse(nearlyEqual(-1.0002f, -1.0001f));
    }

    /** Numbers between 1 and 0 */
    public static void small() {
        System.out.println("---- small ----");
        assertTrue(nearlyEqual(0.000000001000001f, 0.000000001000002f));
        assertTrue(nearlyEqual(0.000000001000002f, 0.000000001000001f));
        assertFalse(nearlyEqual(0.000000000001002f, 0.000000000001001f));
        assertFalse(nearlyEqual(0.000000000001001f, 0.000000000001002f));
    }

    /** Numbers between -1 and 0 */
    public static void smallNeg() {
        System.out.println("---- smallNeg ----");
        assertTrue(nearlyEqual(-0.000000001000001f, -0.000000001000002f));
        assertTrue(nearlyEqual(-0.000000001000002f, -0.000000001000001f));
        assertFalse(nearlyEqual(-0.000000000001002f, -0.000000000001001f));
        assertFalse(nearlyEqual(-0.000000000001001f, -0.000000000001002f));
    }

    /** Small differences away from zero */
    
    public static void smallDiffs() {
        assertTrue(nearlyEqual(0.3f, 0.30000003f));
        assertTrue(nearlyEqual(-0.3f, -0.30000003f));
    }

    /** Comparisons involving zero */
    public static void zero() {
        System.out.println("---- zero ----");
        assertTrue(nearlyEqual(0.0f, 0.0f));
        assertTrue(nearlyEqual(0.0f, -0.0f));
        assertTrue(nearlyEqual(-0.0f, -0.0f));
        assertFalse(nearlyEqual(0.00000001f, 0.0f));
        assertFalse(nearlyEqual(0.0f, 0.00000001f));
        assertFalse(nearlyEqual(-0.00000001f, 0.0f));
        assertFalse(nearlyEqual(0.0f, -0.00000001f));

        assertTrue(nearlyEqual(0.0f, 1e-40f, 0.01f));
        assertTrue(nearlyEqual(1e-40f, 0.0f, 0.01f));
        assertFalse(nearlyEqual(1e-40f, 0.0f, 0.000001f));
        assertFalse(nearlyEqual(0.0f, 1e-40f, 0.000001f));

        assertTrue(nearlyEqual(0.0f, -1e-40f, 0.1f));
        assertTrue(nearlyEqual(-1e-40f, 0.0f, 0.1f));
        assertFalse(nearlyEqual(-1e-40f, 0.0f, 0.00000001f));
        assertFalse(nearlyEqual(0.0f, -1e-40f, 0.00000001f));
    }

    /**
     * Comparisons involving extreme values (overflow potential)
     */
    public static void extremeMax() {
        System.out.println("---- extremeMax ----");
        assertTrue(nearlyEqual(Float.MAX_VALUE, Float.MAX_VALUE));
        assertFalse(nearlyEqual(Float.MAX_VALUE, -Float.MAX_VALUE));
        assertFalse(nearlyEqual(-Float.MAX_VALUE, Float.MAX_VALUE));
        assertFalse(nearlyEqual(Float.MAX_VALUE, Float.MAX_VALUE / 2));
        assertFalse(nearlyEqual(Float.MAX_VALUE, -Float.MAX_VALUE / 2));
        assertFalse(nearlyEqual(-Float.MAX_VALUE, Float.MAX_VALUE / 2));
    }

    /**
     * Comparisons involving infinities
     */
    public static void infinities() {
        System.out.println("---- infinities ----");
        assertTrue(nearlyEqual(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY));
        assertTrue(nearlyEqual(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));
        assertFalse(nearlyEqual(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY));
        assertFalse(nearlyEqual(Float.POSITIVE_INFINITY, Float.MAX_VALUE));
        assertFalse(nearlyEqual(Float.NEGATIVE_INFINITY, -Float.MAX_VALUE));
    }

    /**
     * Comparisons involving NaN values
     */
    public static void nan() {
        System.out.println("---- nan ----");
        assertFalse(nearlyEqual(Float.NaN, Float.NaN));
        assertFalse(nearlyEqual(Float.NaN, 0.0f));
        assertFalse(nearlyEqual(-0.0f, Float.NaN));
        assertFalse(nearlyEqual(Float.NaN, -0.0f));
        assertFalse(nearlyEqual(0.0f, Float.NaN));
        assertFalse(nearlyEqual(Float.NaN, Float.POSITIVE_INFINITY));
        assertFalse(nearlyEqual(Float.POSITIVE_INFINITY, Float.NaN));
        assertFalse(nearlyEqual(Float.NaN, Float.NEGATIVE_INFINITY));
        assertFalse(nearlyEqual(Float.NEGATIVE_INFINITY, Float.NaN));
        assertFalse(nearlyEqual(Float.NaN, Float.MAX_VALUE));
        assertFalse(nearlyEqual(Float.MAX_VALUE, Float.NaN));
        assertFalse(nearlyEqual(Float.NaN, -Float.MAX_VALUE));
        assertFalse(nearlyEqual(-Float.MAX_VALUE, Float.NaN));
        assertFalse(nearlyEqual(Float.NaN, Float.MIN_VALUE));
        assertFalse(nearlyEqual(Float.MIN_VALUE, Float.NaN));
        assertFalse(nearlyEqual(Float.NaN, -Float.MIN_VALUE));
        assertFalse(nearlyEqual(-Float.MIN_VALUE, Float.NaN));
    }

    /** Comparisons of numbers on opposite sides of 0 */
    public static void opposite() {
        System.out.println("---- opposite ----");
        assertFalse(nearlyEqual(1.000000001f, -1.0f));
        assertFalse(nearlyEqual(-1.0f, 1.000000001f));
        assertFalse(nearlyEqual(-1.000000001f, 1.0f));
        assertFalse(nearlyEqual(1.0f, -1.000000001f));
        assertTrue(nearlyEqual(10 * Float.MIN_VALUE, 10 * -Float.MIN_VALUE));
        assertFalse(nearlyEqual(10000 * Float.MIN_VALUE, 10000 * -Float.MIN_VALUE));
    }

    /**
     * The really tricky part - comparisons of numbers very close to zero.
     */
    public static void ulp() {
        System.out.println("---- ulp ----");
        assertTrue(nearlyEqual(Float.MIN_VALUE, Float.MIN_VALUE));
        assertTrue(nearlyEqual(Float.MIN_VALUE, -Float.MIN_VALUE));
        assertTrue(nearlyEqual(-Float.MIN_VALUE, Float.MIN_VALUE));
        assertTrue(nearlyEqual(Float.MIN_VALUE, 0));
        assertTrue(nearlyEqual(0, Float.MIN_VALUE));
        assertTrue(nearlyEqual(-Float.MIN_VALUE, 0));
        assertTrue(nearlyEqual(0, -Float.MIN_VALUE));

        assertFalse(nearlyEqual(0.000000001f, -Float.MIN_VALUE));
        assertFalse(nearlyEqual(0.000000001f, Float.MIN_VALUE));
        assertFalse(nearlyEqual(Float.MIN_VALUE, 0.000000001f));
        assertFalse(nearlyEqual(-Float.MIN_VALUE, 0.000000001f));
    }
}
