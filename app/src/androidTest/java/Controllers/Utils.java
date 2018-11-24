package Controllers;

public class Utils {
    public static String[] nameGen(String startingString, int numToGenerate) {
        String[] returnStringArray = new String[numToGenerate];
        for (int i = 0; i < numToGenerate; i++) {
            returnStringArray[i] = startingString + i;
        }
        return returnStringArray;
    }

}
