import java.io.IOException;
import java.io.FileInputStream;

public class Main {
    private static final int KEY_EVENTS_OFFSET = 18; // Смещение по KeyEvents от K... до ...s
    private static final int KEY_EVENTS_SKIP_BYTE = 2; // Пропуск 1 байта (2 символа)
    private static final int KEY_EVENTS_MIN_OFFSET = 86; // Минимальное расстояние между событиями

    public static void main(String[] args) {

        String filePath = "OTDRs\\Modified OTDR.sor";

        // Получение данных с использованием FileInputStream
        // Данные в файле записаны побайтово: 1 байт - 2 символа в 16-ричной системе.
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            String fileContent = "";
            while (fileInputStream.available() != 0) {
                fileContent = fileContent + String.format("%02X", fileInputStream.read());
            }

            // Преобразование строки "KeyEvents" в необходимый вид, для дальнейшего поиска блока KeyEvents по файлу.
            // Поиск второго(последнего) появление "KeyEvents". KEY_EVENTS_OFFSET(... + 18) - позиция последнего символа "...s"
            int cursor = fileContent.lastIndexOf(compareToHex("KeyEvents")) + KEY_EVENTS_OFFSET + KEY_EVENTS_SKIP_BYTE;
            // Приведение строки с 2 символами в 16-ричной системе к целому числу в 10-чной системе.
            int countEvents = compareToDec(hexScanner(fileContent, cursor, 1));
            System.out.println("Всего зафиксированных событий: " + countEvents);

            // Структура (какие-то данные у которых есть номер события)
            // Сделать через коллекцию. Потом...
            String[] arrayEvent = new String[countEvents];
            for (int i = 0; i < countEvents; i++) {
                String currentEventNumberHex = "00" + compareToHex(i + 1) + "00";
                if (i == 0) {
                    cursor = fileContent.indexOf(currentEventNumberHex, cursor);
                } else {
                    cursor = fileContent.indexOf(currentEventNumberHex, cursor + KEY_EVENTS_MIN_OFFSET);
                }

                arrayEvent[i] = String.valueOf(hexScanner(fileContent, cursor + KEY_EVENTS_SKIP_BYTE, 1));
                System.out.println("Событие №" + i + " = " + arrayEvent[i]);
            }



        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    // Метод преобразования String в String вида HEX
    public static String compareToHex(String inputString) {
        String hexString = "";
        for (int i = 0; i < inputString.length(); i++) {
            hexString = hexString + String.format("%02X", (int)inputString.charAt(i));
        }
        return hexString;
    }

    // Метод преобразования int в String вида HEX
    public static String compareToHex(int inputInt) {
        String hexString = "";
        hexString = hexString + String.format("%02X", inputInt);
        return hexString;
    }

    // Метод преобразования HEX String в int
    public static int compareToDec(String hexString) {
        return Integer.parseInt(hexString, 16);
    }

    // Метод выводит N количество символов из основной строки начиная с определенного значения
    public static String hexScanner(String fileContent, int cursor, int readBytes) {
        String hexString = "";
        for (int i = 0; i < readBytes * 2; i += 2) {
            hexString = hexString + fileContent.charAt(cursor + i) + fileContent.charAt(cursor + i + 1);
        }
        return hexString;
    }

}