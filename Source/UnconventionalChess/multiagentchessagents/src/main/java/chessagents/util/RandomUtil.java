package chessagents.util;

import java.util.Collection;
import java.util.Random;

public class RandomUtil<T> {

    private static final Random random = new Random();

    public static int nextInt(int exclusiveBound) {
        return random.nextInt(exclusiveBound);
    }

    public static boolean randBool() {
        return random.nextBoolean();
    }

    public static float nextFloat() {
        return random.nextFloat();
    }

    public T chooseRandom(Collection<T> collection) {
        var targetIndex = random.nextInt(collection.size());
        var iter = collection.iterator();
        var currentIndex = 0;
        while (currentIndex != targetIndex) {
            iter.next();
            currentIndex++;
        }
        return iter.next();
    }

}
