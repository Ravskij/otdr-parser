import java.io.IOException;
import java.io.FileInputStream;

public class Main {
    private static final int KEY_EVENTS_START_BLOCK = 18; // Смещение до "s" в "KeyEvents"
    private static final int KEY_EVENTS_OFFSET = 86; // Минимальное расстояние между событиями
    private static final int SKIP_BYTE = 2; // Пропуск двух символов (1 байт)

    public static void main(String[] args) {

        int cursor; // Курсор для перемещения по бинарному файлу
        int countEvents; // Количество событий в блоке KeyEvents
        String[] arrayEvent; // Массив событий KeyEvents
        String hexNumber; // десятичное число, приведенное к шестнадцатеричному

        String filePath = "OTDRs\\Modified OTDR.sor";
        String fileContent = fileReader(filePath);

        if (fileContent != null) {
            // Преобразование строки "KeyEvents" к определенному виду для дальнейшего поиска блока KeyEvents по файлу
            cursor = fileContent.lastIndexOf(compareToHex("KeyEvents")) + KEY_EVENTS_START_BLOCK + SKIP_BYTE;
            countEvents = compareToDec(hexScanner(fileContent, cursor, 1));

            // Первое событие находится около курсора, перемещаем его на безопасное расстояние
            cursor -= KEY_EVENTS_OFFSET;
            arrayEvent = new String[countEvents];
            for (int i = 0; i < countEvents; i++) {
                hexNumber = "00" + compareToHex(i + 1) + "00";
                cursor = fileContent.indexOf(hexNumber, cursor + KEY_EVENTS_OFFSET);
                arrayEvent[i] = hexScanner(fileContent, cursor + SKIP_BYTE, 1);
                System.out.println("Событие №" + i + " = " + arrayEvent[i]);
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

    // Метод преобразования int в String вида HEX
    public static String compareToHex(int inputInt) {
        return String.format("%02X", inputInt);
    }

    // Метод приведение строки с 2 символами в 16-ричной системе к целому числу в 10-чной системе.
    public static int compareToDec(String hexString) {
        return Integer.parseInt(hexString, 16);
    }

    // Метод выводит N-ое количество символов из основной строки начиная с определенного значения
    public static String hexScanner(String fileContent, int cursor, int readBytes) {
        StringBuilder hexContent = new StringBuilder();
        for (int i = 0; i < readBytes * SKIP_BYTE; i += SKIP_BYTE) {
            hexContent.append(fileContent.charAt(cursor + i) + fileContent.charAt(cursor + i + 1));
        }
        return hexContent.toString();
    }

    // Метод чтения бинарного файла и записи содержимого в строку
    public static String fileReader(String path) {
        // Получение данных с использованием FileInputStream
        // Данные в файле записаны побайтово: 1 байт - 2 символа в 16-ричной системе.
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            String fileContent = "";
            while (fileInputStream.available() != 0) {
                fileContent = fileContent + String.format("%02X", fileInputStream.read());
            }
            return fileContent;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}