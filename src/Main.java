import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileInputStream;

public class Main {
    private static final int KEY_EVENTS_OFFSET = 18;

    public static void main(String[] args) {
        String filePath = "C:\\Users\\Andrey\\Desktop\\st_A_20242.sor";
        File file = new File(filePath);
        System.out.println(file.getName());
        System.out.println(file.canRead());
        System.out.println(file.canWrite());
        System.out.println(file.canExecute());
        System.out.println(file.exists());
        try(Scanner scanner = new Scanner(file)) {
            System.out.println("cryto");
            while (scanner.hasNext()) {
                System.out.println(scanner.next());
            }
        } catch (FileNotFoundException e) {
            System.out.println("beda " + e.getMessage());
        }

        // Работа с файлом с использованием FileInputStream. Данные записаны побайтово: 1 байт - 2 символа в 16-ричной системе.
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            String fileContent = "";
            while (fileInputStream.available() != 0) {
                fileContent = fileContent + String.format("%02X", fileInputStream.read());
            }

            // Преобразование строки "KeyEvents" в необходимый вид, для дальнейшего поиска блока KeyEvents по файлу.
            String keyEvents = "KeyEvents";
            String hexKeyEvents = "";
            for (int i = 0; i < keyEvents.length(); i++) {
                hexKeyEvents = hexKeyEvents + String.format("%02X", (int)keyEvents.charAt(i));
            }

            // Поиск второго появление "KeyEvents". KEY_EVENTS_OFFSET(... + 18) - позиция последнего символа "...s"
            int lastKeyEventsPosition = fileContent.lastIndexOf(hexKeyEvents) + KEY_EVENTS_OFFSET;

            // Пропуск 1 байта (2 символа)
            String eventCountHex = "" + fileContent.charAt(lastKeyEventsPosition + 2) + fileContent.charAt(lastKeyEventsPosition + 3);
            // Приведение строки с 2 символами в 16-ричной системе к целому числу в 10-чной системе.
            int countEvents = Integer.parseInt(eventCountHex, 16);
            System.out.println("Всего зафиксированных событий: " + countEvents);
        } catch (IOException e) {
            System.out.println(e);
        }

    }
}