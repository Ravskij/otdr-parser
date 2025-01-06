import java.io.IOException;
import java.io.FileInputStream;

public class Main {
    private static final int KEY_EVENTS_START_BLOCK = 18; // Смещение до "s" в "KeyEvents"
    private static final int KEY_EVENTS_OFFSET = 86; // Минимальное расстояние между событиями
    private static final int ONE_BYTE = 2; // Пропуск или считывание двух символов (1 байт)
    private static final int SHORT_INT_HEX = 4;
    private static final int LONG_INT_HEX = 8;
    private static final double SPEED_OF_LIGHT = 299_792_458; // в м/с

    public static void main(String[] args) {

        int countEvents; // Общее количество событий в блоке KeyEvents
        int cursor; // Курсор для перемещения по бинарному файлу
        int eventCursor; // Курсор начала каждого события
        double transitTime; // Время распространения импульса в с
        double refractionIndex = 1.4682; // Коэффициент преломления

        String hexNumber; // Десятичное число, приведенное к шестнадцатеричному
        String[] arrayEvent; // Массив событий KeyEvents

        String filePath = "OTDRs/Modified OTDR.sor";
        String fileContent = fileReader(filePath);

        if (fileContent != null) {
            // Преобразование строки "KeyEvents" к определенному виду для дальнейшего поиска блока KeyEvents по файлу
            eventCursor = fileContent.lastIndexOf(compareToHex("KeyEvents")) + KEY_EVENTS_START_BLOCK + ONE_BYTE;
            countEvents = (int)compareToDec(hexScanner(fileContent, eventCursor, 1));

            // Первое событие находится около курсора, перемещаем его на безопасное расстояние
            eventCursor -= KEY_EVENTS_OFFSET;;
            arrayEvent = new String[countEvents];
            for (int i = 0; i < countEvents; i++) {
                arrayEvent[i] = "";
                hexNumber = compareToHex(i + 1, SHORT_INT_HEX);
                eventCursor = cursor = fileContent.indexOf(hexNumber, eventCursor + KEY_EVENTS_OFFSET);
                arrayEvent[i] += hexScanner(fileContent, cursor, SHORT_INT_HEX) + " "; // Порядковый номер
                cursor += SHORT_INT_HEX;
                transitTime = compareToDec(flipHex(hexScanner(fileContent, cursor + ONE_BYTE, LONG_INT_HEX))) * Math.pow(10, -12); // Время распространения
                arrayEvent[i] += (double)(Math.round(((transitTime * SPEED_OF_LIGHT) / refractionIndex) * 1000)) / 10000 + " "; // Расстояние до события
                cursor += LONG_INT_HEX;
                arrayEvent[i] += (double)compareToDec(hexScanner(fileContent, cursor, SHORT_INT_HEX)) / 1000 + " "; // Километрическое затухание
                cursor += SHORT_INT_HEX + ONE_BYTE;
                if (hexScanner(fileContent, cursor + ONE_BYTE, ONE_BYTE).equals("FF")) { // Затухание на событии
                    arrayEvent[i] += -(double)compareToDec(invertHex(flipHex(hexScanner(fileContent, cursor, SHORT_INT_HEX) + "FFFF"))) / 1000 + " ";
                } else {
                    arrayEvent[i] += (double)compareToDec(flipHex(hexScanner(fileContent, cursor, SHORT_INT_HEX))) / 1000 + " ";
                }
                cursor += SHORT_INT_HEX;
                arrayEvent[i] += -(double)compareToDec(invertHex(flipHex(hexScanner(fileContent, cursor, LONG_INT_HEX)))) / 1000 + " "; // Коэффициент отражения
                cursor += LONG_INT_HEX;
                System.out.println("Событие №" + arrayEvent[i]);


            }
            System.out.println("Всего зафиксированных событий: " + countEvents);

        } else {
            System.out.println("Файл не найден или не удалось прочитать файл");
        }
    }

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
        for (int i = 0; i < readBytes; i += ONE_BYTE) {
            hexContent.append(fileContent.charAt(cursor + i));
            hexContent.append(fileContent.charAt(cursor + i + 1));
        }
        return hexContent.toString();
    }

    // Чтение данных из бинарного файла и запись содержимого в строку
    public static String fileReader(String path) {
        // Данные в файле записаны побайтово: 1 байт - 2 символа в 16-ричной системе.
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            StringBuilder fileContent = new StringBuilder();
            while (fileInputStream.available() != 0) {
                fileContent.append(String.format("%02X", fileInputStream.read()));
            }
            return fileContent.toString();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Побайтовый переворот 16-ричного числа
    public static String flipHex(String hexNumber) {
        StringBuilder negativeHexNumber = new StringBuilder();
        for (int i = hexNumber.length() - 1; i > 0; i-= ONE_BYTE) {
            negativeHexNumber.append(hexNumber.charAt(i - 1));
            negativeHexNumber.append(hexNumber.charAt(i));
        }
        return negativeHexNumber.toString();
    }

    // Побитовое инвертирование 16-ричного числа
    public static String invertHex(String hexNumber) {
        long value = ~compareToDec(hexNumber) + 1 & 0xFFFFFFFFL;
        return compareToHex((int)value, LONG_INT_HEX);
    }

}