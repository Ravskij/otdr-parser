public class EventProcessing {
    static final int START_BLOCK = 18; // Смещение до "s" в "KeyEvents" или "FxdParams"
    static final int KEY_EVENTS_OFFSET = 86; // Минимальное расстояние между событиями
    static final int ONE_BYTE = 2; // Пропуск или считывание двух символов (1 байт)
    static final int SHORT_INT_HEX = 4; // Число символов, соответствующее short int (2 байта) в hex формате
    static final int LONG_INT_HEX = 8; // Число символов, соответствующее long int (4 байта) в hex формате
    static final double SPEED_OF_LIGHT = 299_792_458; // Скорость света в вакууме, м/с
    static final double REFRACTION_INDEX = 1.4682; // Коэффициент преломления оптического волокна

    static int cursor; // Курсор для перемещения по бинарному файлу

    public static String extractParameters(String hexContent) {
        String date; // Дата и время измерений
        String distanceUnits; // Единицы измерения расстояния

        // Преобразование строки "FxdParams" к определенному виду для дальнейшего поиска блока FxdParams по файлу
        cursor = hexContent.lastIndexOf(HexKit.compareToHex("FxdParams")) + START_BLOCK + ONE_BYTE;
        date = String.valueOf(HexKit.compareToDec(HexKit.hexScanner(hexContent, cursor, LONG_INT_HEX)));
        System.out.println(date);
        cursor += LONG_INT_HEX;
        distanceUnits = "" + (char)HexKit.compareToDec(HexKit.hexScanner(hexContent, cursor, ONE_BYTE)) +
                (char)HexKit.compareToDec(HexKit.hexScanner(hexContent, cursor + ONE_BYTE, ONE_BYTE));
        System.out.println(distanceUnits);
        return "";
    }

    public static String[] extractEvents(String hexContent) {
        int countEvents; // Общее количество событий в блоке KeyEvents
        int eventCursor; // Курсор начала каждого события
        double transitTime; // Время распространения импульса в с

        String hexNumber; // Десятичное число, приведенное к шестнадцатеричному
        StringBuilder[] arrayEventBuilder; // Массив построения событий KeyEvents
        String[] arrayEven; // Массив событий KeyEvents

        // Преобразование строки "KeyEvents" к определенному виду для дальнейшего поиска блока KeyEvents по файлу
        eventCursor = hexContent.lastIndexOf(HexKit.compareToHex("KeyEvents")) + START_BLOCK + ONE_BYTE;
        countEvents = (int) HexKit.compareToDec(HexKit.hexScanner(hexContent, eventCursor, 1));

        // Первое событие находится около курсора, перемещаем его на безопасное расстояние
        eventCursor -= KEY_EVENTS_OFFSET;
        arrayEventBuilder = new StringBuilder[countEvents];
        arrayEven = new String[countEvents];
        for (int i = 0; i < countEvents; i++) {
            arrayEventBuilder[i] = new StringBuilder();
            hexNumber = HexKit.compareToHex(i + 1, SHORT_INT_HEX);
            eventCursor = cursor = hexContent.indexOf(hexNumber, eventCursor + KEY_EVENTS_OFFSET);
            arrayEventBuilder[i].append(HexKit.compareToDec(HexKit.hexScanner(hexContent, cursor, SHORT_INT_HEX))).append("\t"); // Порядковый номер
            cursor += SHORT_INT_HEX;
            transitTime = HexKit.compareToDec(HexKit.flipHex(HexKit.hexScanner(hexContent, cursor + ONE_BYTE, LONG_INT_HEX))) * Math.pow(10, -12); // Время распространения
            arrayEventBuilder[i].append((double) (Math.round(((transitTime * SPEED_OF_LIGHT) / REFRACTION_INDEX) * 1000)) / 10000).append("\t"); // Расстояние до события
            cursor += LONG_INT_HEX;
            arrayEventBuilder[i].append((double) HexKit.compareToDec(HexKit.hexScanner(hexContent, cursor, SHORT_INT_HEX)) / 1000).append("\t"); // Километрическое затухание
            cursor += SHORT_INT_HEX + ONE_BYTE;
            if (HexKit.hexScanner(hexContent, cursor + ONE_BYTE, ONE_BYTE).equals("FF")) { // Затухание на событии
                arrayEventBuilder[i].append(-(double) HexKit.compareToDec(HexKit.invertHex(HexKit.flipHex(HexKit.hexScanner(hexContent, cursor, SHORT_INT_HEX) + "FFFF"))) / 1000).append("\t");
            } else {
                arrayEventBuilder[i].append((double) HexKit.compareToDec(HexKit.flipHex(HexKit.hexScanner(hexContent, cursor, SHORT_INT_HEX))) / 1000).append("\t");
            }
            cursor += SHORT_INT_HEX;
            arrayEventBuilder[i].append(-(double) HexKit.compareToDec(HexKit.invertHex(HexKit.flipHex(HexKit.hexScanner(hexContent, cursor, LONG_INT_HEX)))) / 1000); // Коэффициент отражения
            System.out.println("Событие №" + arrayEventBuilder[i]);
        }
        System.out.println("Всего зафиксированных событий: " + countEvents);
        arrayEven = convertToStringArray(arrayEventBuilder);
        return arrayEven;
    }

    public static String[] convertToStringArray(StringBuilder[] arrayEvent) {
        String[] outputString = new String[arrayEvent.length];
        for (int i = 0; i < arrayEvent.length; i++) {
            outputString[i] = arrayEvent[i].toString();
        }
        return outputString;
    }
}
