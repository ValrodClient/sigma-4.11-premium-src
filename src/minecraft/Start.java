import java.util.Arrays;

public class Start {

    public static void main(String[] args) {
        original(args);
    }

    private static void original(String[] args) {
        Main.main(concat(new String[]{"--version", "1.8-OptiFine_HD_U_H6", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"}, args));
    }

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

}
