package core.utils;

public class Toolbox
{
    private static Toolbox instance;

    public static String trim(String text, String trimBy) {
        int beginIndex = 0;
        int endIndex = text.length();

        while (text.substring(beginIndex, endIndex).startsWith(trimBy)) {
            beginIndex += trimBy.length();
        }

        while (text.substring(beginIndex, endIndex).endsWith(trimBy)) {
            endIndex -= trimBy.length();
        }

        return text.substring(beginIndex, endIndex);
    }

    public static boolean equals(long l1, long l2)
    {
        return l1 == l2;
    }
}
