public class HexKit {

    // Метод преобразования String в String вида HEX
    public static String compareToHex(String inputString) {
        StringBuilder hexContent = new StringBuilder();
        for (int i = 0; i < inputString.length(); i++) {
            hexContent.append(String.format("%02X", (int)inputString.charAt(i)));
        }
        return hexContent.toString();
    }

    // Преобразование int в String вида HEX
    public static String compareToHex(int inputInt, int countSymbols) {
        return String.format("%0" + countSymbols + "X", inputInt);
    }

    // Приведение строки с 2 символами в 16-ричной системе к целому числу в 10-чной системе.
    public static long compareToDec(String hexString) {
        return Long.parseLong(hexString, 16);
    }

    // Вывод N-ого количества символов из основной строки начиная с определенного значения
    public static String hexScanner(String fileContent, int cursor, int readBytes) {
        StringBuilder hexContent = new StringBuilder();
        for (int i = 0; i < readBytes; i += HexContentProcessing.ONE_BYTE) {
            hexContent.append(fileContent.charAt(cursor + i));
            hexContent.append(fileContent.charAt(cursor + i + 1));
        }
        return hexContent.toString();
    }

    // Побайтовый переворот 16-ричного числа
    public static String flipHex(String hexNumber) {
        StringBuilder negativeHexNumber = new StringBuilder();
        for (int i = hexNumber.length() - 1; i > 0; i-= HexContentProcessing.ONE_BYTE) {
            negativeHexNumber.append(hexNumber.charAt(i - 1));
            negativeHexNumber.append(hexNumber.charAt(i));
        }
        return negativeHexNumber.toString();
    }

    // Побитовое инвертирование 16-ричного числа
    public static String invertHex(String hexNumber) {
        long value = ~compareToDec(hexNumber) + 1 & 0xFFFFFFFFL;
        return compareToHex((int)value, HexContentProcessing.LONG_INT_HEX);
    }

}
