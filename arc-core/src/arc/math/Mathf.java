package arc.math;

import arc.util.Time;

import java.util.Random;

public final class Mathf{
    public static final int[] signs = {-1, 1};
    public static final boolean[] booleans = {true, false};
    public static final float FLOAT_ROUNDING_ERROR = 0.000001f; // 32 bits
    public static final float PI = 3.1415927f, pi = PI;
    public static final float PI2 = PI * 2;
    public static final float E = 2.7182818f;
    public static final float sqrt2 = Mathf.sqrt(2f);
    public static final float sqrt3 = Mathf.sqrt(3f);
    /** multiply by this to convert from radians to degrees */
    public static final float radiansToDegrees = 180f / PI;
    public static final float radDeg = radiansToDegrees;
    /** multiply by this to convert from degrees to radians */
    public static final float degreesToRadians = PI / 180;
    public static final float degRad = degreesToRadians;
    static private final int SIN_BITS = 14; // 16KB. Adjust for accuracy.
    static private final int SIN_MASK = ~(-1 << SIN_BITS);
    static private final int SIN_COUNT = SIN_MASK + 1;
    static private final float radFull = PI * 2;
    static private final float degFull = 360;
    static private final float radToIndex = SIN_COUNT / radFull;
    static private final float degToIndex = SIN_COUNT / degFull;
    static private final int BIG_ENOUGH_INT = 16 * 1024;
    static private final double BIG_ENOUGH_FLOOR = BIG_ENOUGH_INT;
    static private final double CEIL = 0.9999999;
    static private final double BIG_ENOUGH_CEIL = 16384.999999999996;
    static private final double BIG_ENOUGH_ROUND = BIG_ENOUGH_INT + 0.5f;
    static private final Rand seedr = new Rand();

    public static Random random = new Rand();

    /** Returns the sine in radians from a lookup table. */
    public static float sin(float radians){
        return Sin.table[(int)(radians * radToIndex) & SIN_MASK];
    }

    /** Returns the cosine in radians from a lookup table. */
    public static float cos(float radians){
        return Sin.table[(int)((radians + PI / 2) * radToIndex) & SIN_MASK];
    }

    /** Returns the sine in radians from a lookup table. */
    public static float sinDeg(float degrees){
        return Sin.table[(int)(degrees * degToIndex) & SIN_MASK];
    }

    /** Returns the cosine in radians from a lookup table. */
    public static float cosDeg(float degrees){
        return Sin.table[(int)((degrees + 90) * degToIndex) & SIN_MASK];
    }

    public static float absin(float scl, float mag){
        return absin(Time.time(), scl, mag);
    }

    public static float absin(float in, float scl, float mag){
        return (sin(in, scl * 2f, mag) + mag) / 2f;
    }

    public static float tan(float radians, float scl, float mag){
        return (sin(radians / scl)) / (cos(radians / scl)) * mag;
    }

    public static float sin(float scl, float mag){
        return sin(Time.time() / scl) * mag;
    }

    public static float sin(float radians, float scl, float mag){
        return sin(radians / scl) * mag;
    }

    public static float cos(float radians, float scl, float mag){
        return cos(radians / scl) * mag;
    }

    public static float angle(float x, float y){
        float result = atan2(x, y) * radDeg;
        if(result < 0) result += 360f;
        return result;
    }

    public static float angleExact(float x, float y){
        float result = (float)Math.atan2(y, x) * radDeg;
        if(result < 0) result += 360f;
        return result;
    }

    /**
     * Returns atan2 in radians, faster but less accurate than Math.atan2. Average error of 0.00231 radians (0.1323 degrees),
     * largest error of 0.00488 radians (0.2796 degrees).
     */
    public static float atan2(float x, float y){
        if(Math.abs(x) < 0.0000001f){
            if(y > 0f) return PI / 2;
            if(y == 0f) return 0f;
            return -PI / 2;
        }
        final float atan, z = y / x;
        if(Math.abs(z) < 1f){
            atan = z / (1f + 0.28f * z * z);
            if(x < 0f) return atan + (y < 0f ? -PI : PI);
            return atan;
        }
        atan = PI / 2 - z / (z * z + 0.28f);
        return y < 0f ? atan - PI : atan;
    }

    public static float sqrt(float x){
        return (float) Math.sqrt(x);
    }

    /**Returns -1 if f<0, 1 otherwise.*/
    public static int sign(float f){
        return (f < 0 ? -1 : 1);
    }

    /** Returns 1 if true, -1 if false. */
    public static int sign(boolean b){
        return b ? 1 : -1;
    }

    /**Converts a boolean to an integer: 1 if true, 0, if false.*/
    public static int num(boolean b){
        return b ? 1 : 0;
    }

    public static float pow(float a, float b){
        return (float) Math.pow(a, b);
    }

    public static float range(float range){
        return random(-range, range);
    }

    public static int range(int range){
        return random(-range, range);
    }

    public static float range(float min, float max){
        if(chance(0.5)){
            return random(min, max);
        }else{
            return -random(min, max);
        }
    }

    public static boolean chance(double d){
        return random.nextFloat() < d;
    }

    /** Returns a random number between 0 (inclusive) and the specified value (inclusive). */
    public static int random(int range){
        return random.nextInt(range + 1);
    }

    /** Returns a random number between start (inclusive) and end (inclusive). */
    public static int random(int start, int end){
        return start + random.nextInt(end - start + 1);
    }

    /** Returns a random number between 0 (inclusive) and the specified value (inclusive). */
    public static long random(long range){
        return (long)(random.nextDouble() * range);
    }

    /** Returns a random number between start (inclusive) and end (inclusive). */
    public static long random(long start, long end){
        return start + (long)(random.nextDouble() * (end - start));
    }

    /** Returns a random boolean value. */
    public static boolean randomBoolean(){
        return random.nextBoolean();
    }

    /** Returns true if a random value between 0 and 1 is less than the specified value. */
    public static boolean randomBoolean(float chance){
        return Mathf.random() < chance;
    }

    /** Returns random number between 0.0 (inclusive) and 1.0 (exclusive). */
    public static float random(){
        return random.nextFloat();
    }

    /** Returns a random number between 0 (inclusive) and the specified value (exclusive). */
    public static float random(float range){
        return random.nextFloat() * range;
    }

    /** Returns a random number between start (inclusive) and end (exclusive). */
    public static float random(float start, float end){
        return start + random.nextFloat() * (end - start);
    }

    /** Returns -1 or 1, randomly. */
    public static int randomSign(){
        return 1 | (random.nextInt() >> 31);
    }

    /** Inclusive. */
    public static int randomSeed(long seed, int min, int max){
        seedr.setSeed(seed);
        if(isPowerOfTwo(max)){
            seedr.nextInt();
        }
        return seedr.nextInt(max - min + 1) + min;
    }

    public static float randomSeed(long seed){
        seedr.setSeed(seed * 99999);
        return seedr.nextFloat();
    }

    public static float randomSeedRange(long seed, float range){
        seedr.setSeed(seed * 99999);
        return range * (seedr.nextFloat() - 0.5f) * 2f;
    }

    /**
     * Returns a triangularly distributed random number between -1.0 (exclusive) and 1.0 (exclusive), where values around zero are
     * more likely.
     * <p>
     * This is an optimized version of {@link #randomTriangular(float, float, float) randomTriangular(-1, 1, 0)}
     */
    public static float randomTriangular(){
        return random.nextFloat() - random.nextFloat();
    }

    /**
     * Returns a triangularly distributed random number between {@code -max} (exclusive) and {@code max} (exclusive), where values
     * around zero are more likely.
     * <p>
     * This is an optimized version of {@link #randomTriangular(float, float, float) randomTriangular(-max, max, 0)}
     * @param max the upper limit
     */
    public static float randomTriangular(float max){
        return (random.nextFloat() - random.nextFloat()) * max;
    }

    /**
     * Returns a triangularly distributed random number between {@code min} (inclusive) and {@code max} (exclusive), where the
     * {@code mode} argument defaults to the midpoint between the bounds, giving a symmetric distribution.
     * <p>
     * This method is equivalent of {@link #randomTriangular(float, float, float) randomTriangular(min, max, (min + max) * .5f)}
     * @param min the lower limit
     * @param max the upper limit
     */
    public static float randomTriangular(float min, float max){
        return randomTriangular(min, max, (min + max) * 0.5f);
    }

    /**
     * Returns a triangularly distributed random number between {@code min} (inclusive) and {@code max} (exclusive), where values
     * around {@code mode} are more likely.
     * @param min the lower limit
     * @param max the upper limit
     * @param mode the point around which the values are more likely
     */
    public static float randomTriangular(float min, float max, float mode){
        float u = random.nextFloat();
        float d = max - min;
        if(u <= (mode - min) / d) return min + (float)Math.sqrt(u * d * (mode - min));
        return max - (float)Math.sqrt((1 - u) * d * (max - mode));
    }

    /** Returns the next power of two. Returns the specified value if the value is already a power of two. */
    public static int nextPowerOfTwo(int value){
        if(value == 0) return 1;
        value--;
        value |= value >> 1;
        value |= value >> 2;
        value |= value >> 4;
        value |= value >> 8;
        value |= value >> 16;
        return value + 1;
    }

    public static boolean isPowerOfTwo(int value){
        return value != 0 && (value & value - 1) == 0;
    }

    public static short clamp(short value, short min, short max){
        if(value < min) return min;
        if(value > max) return max;
        return value;
    }

    public static int clamp(int value, int min, int max){
        if(value < min) return min;
        if(value > max) return max;
        return value;
    }

    public static long clamp(long value, long min, long max){
        if(value < min) return min;
        if(value > max) return max;
        return value;
    }

    public static float clamp(float value, float min, float max){
        if(value < min) return min;
        if(value > max) return max;
        return value;
    }

    /** Clamps to [0, 1]. */
    public static float clamp(float value){
        return clamp(value, 0f, 1f);
    }

    public static double clamp(double value, double min, double max){
        if(value < min) return min;
        if(value > max) return max;
        return value;
    }

    /** Linearly interpolates between fromValue to toValue on progress position. */
    public static float lerp(float fromValue, float toValue, float progress){
        return fromValue + (toValue - fromValue) * progress;
    }

    /** Linearly interpolates between fromValue to toValue on progress position. Multiplied by Time.delta().*/
    public static float lerpDelta(float fromValue, float toValue, float progress){
        return lerp(fromValue, toValue, clamp(progress * Time.delta()));
    }

    /**
     * Linearly interpolates between two angles in radians. Takes into account that angles wrap at two pi and always takes the
     * direction with the smallest delta angle.
     * @param fromRadians start angle in radians
     * @param toRadians target angle in radians
     * @param progress interpolation value in the range [0, 1]
     * @return the interpolated angle in the range [0, PI2[
     */
    public static float slerpRad(float fromRadians, float toRadians, float progress){
        float delta = ((toRadians - fromRadians + PI2 + PI) % PI2) - PI;
        return (fromRadians + delta * progress + PI2) % PI2;
    }

    /**
     * Linearly interpolates between two angles in degrees. Takes into account that angles wrap at 360 degrees and always takes
     * the direction with the smallest delta angle.
     * @param fromDegrees start angle in degrees
     * @param toDegrees target angle in degrees
     * @param progress interpolation value in the range [0, 1]
     * @return the interpolated angle in the range [0, 360[
     */
    public static float slerp(float fromDegrees, float toDegrees, float progress){
        float delta = ((toDegrees - fromDegrees + 360 + 180) % 360) - 180;
        return (fromDegrees + delta * progress + 360) % 360;
    }

    public static float slerpDelta(float fromDegrees, float toDegrees, float progress){
        return slerp(fromDegrees, toDegrees, clamp(progress * Time.delta()));
    }

    /**
     * Returns the largest integer less than or equal to the specified float. This method will only properly floor floats from
     * -(2^14) to (Float.MAX_VALUE - 2^14).
     */
    public static int floor(float value){
        return (int)(value + BIG_ENOUGH_FLOOR) - BIG_ENOUGH_INT;
    }

    /**
     * Returns the largest integer less than or equal to the specified float. This method will only properly floor floats that are
     * positive. Note this method simply casts the float to int.
     */
    public static int floorPositive(float value){
        return (int)value;
    }

    /**
     * Returns the smallest integer greater than or equal to the specified float. This method will only properly ceil floats from
     * -(2^14) to (Float.MAX_VALUE - 2^14).
     */
    public static int ceil(float value){
        return BIG_ENOUGH_INT - (int)(BIG_ENOUGH_FLOOR - value);
    }

    /**
     * Returns the smallest integer greater than or equal to the specified float. This method will only properly ceil floats that
     * are positive.
     */
    public static int ceilPositive(float value){
        return (int)(value + CEIL);
    }

    /**
     * Returns the closest integer to the specified float. This method will only properly round floats from -(2^14) to
     * (Float.MAX_VALUE - 2^14).
     */
    public static int round(float value){
        return (int)(value + BIG_ENOUGH_ROUND) - BIG_ENOUGH_INT;
    }

    public static float round(float value, float step){
        return (int)(value / step) * step;
    }

    public static int round(float value, int step){
        return (int)(value / step) * step;
    }

    /** Returns the closest integer to the specified float. This method will only properly round floats that are positive. */
    public static int roundPositive(float value){
        return (int)(value + 0.5f);
    }

    /** Returns true if the value is zero (using the default tolerance as upper bound) */
    public static boolean zero(float value){
        return Math.abs(value) <= FLOAT_ROUNDING_ERROR;
    }

    /**
     * Returns true if the value is zero.
     * @param tolerance represent an upper bound below which the value is considered zero.
     */
    public static boolean zero(float value, float tolerance){
        return Math.abs(value) <= tolerance;
    }

    /**
     * Returns true if a is nearly equal to b. The function uses the default floating error tolerance.
     * @param a the first value.
     * @param b the second value.
     */
    public static boolean equal(float a, float b){
        return Math.abs(a - b) <= FLOAT_ROUNDING_ERROR;
    }

    /**
     * Returns true if a is nearly equal to b.
     * @param a the first value.
     * @param b the second value.
     * @param tolerance represent an upper bound below which the two values are considered equal.
     */
    public static boolean equal(float a, float b, float tolerance){
        return Math.abs(a - b) <= tolerance;
    }

    /** @return the logarithm of value with base a */
    public static float log(float a, float value){
        return (float)(Math.log(value) / Math.log(a));
    }

    /** @return the logarithm of value with base 2 */
    public static float log2(float value){
        return (float)Math.log(value) / 0.301029996f;
    }

    /** Mod function that works properly for negative numbers. */
    public static float mod(float x, float n){
        return ((x % n) + n) % n;
    }

    /** Mod function that works properly for negative numbers. */
    public static int mod(int x, int n){
        return ((x % n) + n) % n;
    }

    /**Converts a 0-1 value to 0-1 when it is in [0, offset].*/
    public static float curve(float f, float offset){
        if(f < offset){
            return 0f;
        }else{
            return (f - offset) / (1f - offset);
        }
    }

    /**Converts a 0-1 value to 0-1 when it is in [0, to].*/
    public static float curve(float f, float offset, float to){
        if(f < offset){
            return 0f;
        }else if(f - offset > to){
            return 1f;
        }else{
            return (f - offset) / to;
        }
    }

    public static float len(float x, float y){
        return (float)Math.sqrt(x * x + y * y);
    }

    public static float len2(float x, float y){
        return x * x + y * y;
    }

    public static float dot(float x1, float y1, float x2, float y2){
        return x1 * x2 + y1 * y2;
    }

    public static float dst(float x1, float y1){
        return (float)Math.sqrt(x1 * x1 + y1*y1);
    }

    public static float dst2(float x1, float y1){
        return (x1 * x1 + y1*y1);
    }

    public static float dst(float x1, float y1, float x2, float y2){
        final float x_d = x2 - x1;
        final float y_d = y2 - y1;
        return (float)Math.sqrt(x_d * x_d + y_d * y_d);
    }

    public static float dst2(float x1, float y1, float x2, float y2){
        final float x_d = x2 - x1;
        final float y_d = y2 - y1;
        return x_d * x_d + y_d * y_d;
    }

    public static boolean within(float x1, float y1, float x2, float y2, float dst){
        return dst2(x1, y1, x2, y2) < dst*dst;
    }

    public static boolean within(float x1, float y1, float dst){
        return dst2(x1, y1) < dst*dst;
    }

    static private class Sin{
        static final float[] table = new float[SIN_COUNT];

        static{
            for(int i = 0; i < SIN_COUNT; i++)
                table[i] = (float)Math.sin((i + 0.5f) / SIN_COUNT * radFull);
            for(int i = 0; i < 360; i += 90)
                table[(int)(i * degToIndex) & SIN_MASK] = (float)Math.sin(i * degreesToRadians);
        }
    }
}