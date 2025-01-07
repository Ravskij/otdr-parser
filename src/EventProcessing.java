import java.io.IOException;

public class EventProcessing {
    static final int KEY_EVENTS_START_BLOCK = 18; // Смещение до "s" в "KeyEvents"
    static final int KEY_EVENTS_OFFSET = 86; // Минимальное расстояние между событиями
    static final int ONE_BYTE = 2; // Пропуск или считывание двух символов (1 байт)
    static final int SHORT_INT_HEX = 4;
    static final int LONG_INT_HEX = 8;
    static final double SPEED_OF_LIGHT = 299_792_458; // в м/с

    public static String[] processEvents(String fileContent) throws IOException {
        int countEvents; // Общее количество событий в блоке KeyEvents
        int cursor; // Курсор для перемещения по бинарному файлу
        int eventCursor; // Курсор начала каждого события
        double transitTime; // Время распространения импульса в с
        double refractionIndex = 1.4682; // Коэффициент преломления

        String hexNumber; // Десятичное число, приведенное к шестнадцатеричному
        String[] arrayEvent; // Массив событий KeyEvents

        // Преобразование строки "KeyEvents" к определенному виду для дальнейшего поиска блока KeyEvents по файлу
        eventCursor = fileContent.lastIndexOf(HexKit.compareToHex("KeyEvents")) + KEY_EVENTS_START_BLOCK + ONE_BYTE;
        countEvents = (int) HexKit.compareToDec(HexKit.hexScanner(fileContent, eventCursor, 1));

        // Первое событие находится около курсора, перемещаем его на безопасное расстояние
        eventCursor -= KEY_EVENTS_OFFSET;
        arrayEvent = new String[countEvents];
        for (int i = 0; i < countEvents; i++) {
            arrayEvent[i] = "";
            hexNumber = HexKit.compareToHex(i + 1, SHORT_INT_HEX);
            eventCursor = cursor = fileContent.indexOf(hexNumber, eventCursor + KEY_EVENTS_OFFSET);
            arrayEvent[i] += HexKit.compareToDec(HexKit.hexScanner(fileContent, cursor, SHORT_INT_HEX)) + " "; // Порядковый номер
            cursor += SHORT_INT_HEX;
            transitTime = HexKit.compareToDec(HexKit.flipHex(HexKit.hexScanner(fileContent, cursor + ONE_BYTE, LONG_INT_HEX))) * Math.pow(10, -12); // Время распространения
            arrayEvent[i] += (double) (Math.round(((transitTime * SPEED_OF_LIGHT) / refractionIndex) * 1000)) / 10000 + " "; // Расстояние до события
            cursor += LONG_INT_HEX;
            arrayEvent[i] += (double) HexKit.compareToDec(HexKit.hexScanner(fileContent, cursor, SHORT_INT_HEX)) / 1000 + " "; // Километрическое затухание
            cursor += SHORT_INT_HEX + ONE_BYTE;
            if (HexKit.hexScanner(fileContent, cursor + ONE_BYTE, ONE_BYTE).equals("FF")) { // Затухание на событии
                arrayEvent[i] += -(double) HexKit.compareToDec(HexKit.invertHex(HexKit.flipHex(HexKit.hexScanner(fileContent, cursor, SHORT_INT_HEX) + "FFFF"))) / 1000 + " ";
            } else {
                arrayEvent[i] += (double) HexKit.compareToDec(HexKit.flipHex(HexKit.hexScanner(fileContent, cursor, SHORT_INT_HEX))) / 1000 + " ";
            }
            cursor += SHORT_INT_HEX;
            arrayEvent[i] += -(double) HexKit.compareToDec(HexKit.invertHex(HexKit.flipHex(HexKit.hexScanner(fileContent, cursor, LONG_INT_HEX)))) / 1000; // Коэффициент отражения
            System.out.println("Событие №" + arrayEvent[i]);
        }
        System.out.println("Всего зафиксированных событий: " + countEvents);
        return arrayEvent;
    }
}
