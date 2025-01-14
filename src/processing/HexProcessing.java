package processing;

import static processing.HexKit.*;
import static processing.Constants.*;

public class HexProcessing {

    static int cursor; // Курсор для перемещения по бинарному файлу

    // Обработка блока FxdParams
    public static String extractParameters(String hexContent) {
        String date; // Дата и время измерений
        String distanceUnits; // Единицы измерения расстояния
        String wavelength; // Длина волны

        // Массив для построения событий из блока FxdParameters
        StringBuilder arrayFxdParametersBuilder;
        arrayFxdParametersBuilder = new StringBuilder();

        // Преобразование строки "FxdParams" к определенному виду для дальнейшего поиска блока FxdParams по файлу
        cursor = hexContent.lastIndexOf(HexKit.compareToHex("FxdParams")) + START_BLOCK + ONE_BYTE;
        date = String.valueOf(HexKit.compareToDec(HexKit.hexScanner(hexContent, cursor, LONG_INT_HEX)));
        arrayFxdParametersBuilder.append("Дата и время измерения: ").append(date).append("\n");
        cursor += LONG_INT_HEX;
        distanceUnits = "" + (char)HexKit.compareToDec(HexKit.hexScanner(hexContent, cursor, ONE_BYTE)) +
                (char)HexKit.compareToDec(HexKit.hexScanner(hexContent, cursor + ONE_BYTE, ONE_BYTE));
        arrayFxdParametersBuilder.append("Единицы измерения расстояния: ").append(distanceUnits).append("\n");
        cursor += ONE_BYTE + ONE_BYTE;
        wavelength = "" + HexKit.compareToDec(HexKit.flipHex(HexKit.hexScanner(hexContent, cursor, SHORT_INT_HEX))) / 10;
        arrayFxdParametersBuilder.append("Длина волны: ").append(wavelength).append(" нм").append("\n");
        cursor += SHORT_INT_HEX;

        return arrayFxdParametersBuilder.toString();
    }

    // Обработка блока KeyEvents
    public static String[] extractEvents(String hexContent) {
        int countEvents; // Общее количество событий в блоке KeyEvents
        int eventCursor; // Курсор начала каждого события
        double transitTime; // Время распространения импульса в с
        double reflectionIndex;

        String hexNumber; // Десятичное число, приведенное к шестнадцатеричному
        StringBuilder[] arrayEventBuilder; // Массив для построения событий из блока KeyEvents
        String[] arrayEven; // Массив событий KeyEvents

        // Преобразование строки "KeyEvents" к определенному виду для дальнейшего поиска блока KeyEvents по файлу
        eventCursor = hexContent.lastIndexOf(compareToHex("KeyEvents")) + START_BLOCK + ONE_BYTE;
        countEvents = (int) compareToDec(hexScanner(hexContent, eventCursor, ONE_BYTE));

        // Первое событие находится около курсора, перемещаем его на безопасное расстояние
        eventCursor -= KEY_EVENTS_OFFSET;
        arrayEventBuilder = new StringBuilder[countEvents];
        for (int i = 0; i < countEvents; i++) {
            arrayEventBuilder[i] = new StringBuilder();
            hexNumber = compareToHex(i + 1, SHORT_INT_HEX);
            eventCursor = cursor = hexContent.indexOf(hexNumber, eventCursor + KEY_EVENTS_OFFSET);
            if (i != 0) { // первое (нулевое) событие пропускается
                arrayEventBuilder[i].append(i).append("\t"); // Порядковый номер
                cursor += SHORT_INT_HEX;
                transitTime = compareToDec(flipHex(hexScanner(hexContent, cursor + ONE_BYTE, LONG_INT_HEX))) * Math.pow(10, -12); // Время распространения
                arrayEventBuilder[i].append((double) (Math.round(((transitTime * SPEED_OF_LIGHT) / REFRACTION_INDEX) * 1000)) / 10000).append("\t"); // Расстояние до события
                cursor += LONG_INT_HEX;
                arrayEventBuilder[i].append((double) compareToDec(hexScanner(hexContent, cursor, SHORT_INT_HEX)) / 1000).append("\t"); // Километрическое затухание
                cursor += SHORT_INT_HEX + ONE_BYTE;
                if (hexScanner(hexContent, cursor + ONE_BYTE, ONE_BYTE).equals("FF")) { // Затухание на событии
                    arrayEventBuilder[i].append(-(double) compareToDec(invertHex(flipHex(hexScanner(hexContent, cursor, SHORT_INT_HEX) + "FFFF"))) / 1000).append("\t");
                } else {
                    arrayEventBuilder[i].append((double) compareToDec(flipHex(hexScanner(hexContent, cursor, SHORT_INT_HEX))) / 1000).append("\t");
                }
                cursor += SHORT_INT_HEX;
                reflectionIndex = -(double) compareToDec(invertHex(flipHex(hexScanner(hexContent, cursor, LONG_INT_HEX)))) / 1000; // Коэффициент отражения
                if (reflectionIndex < -65) {
                    arrayEventBuilder[i].append("<-65.000");
                } else if (reflectionIndex > 14) {
                    arrayEventBuilder[i].append(">14.000");
                } else {
                    arrayEventBuilder[i].append(reflectionIndex);
                }
            } else {
                arrayEventBuilder[i].append(i).append("\t"); // Порядковый номер
                arrayEventBuilder[0].append("0.0000").append("\t"); // Расстояние
                arrayEventBuilder[0].append("-----").append("\t");
                arrayEventBuilder[0].append("-----").append("\t");
                arrayEventBuilder[0].append("-----");
            }
            System.out.println("Событие №" + arrayEventBuilder[i]);
        }
        System.out.println("Всего зафиксированных событий: " + countEvents);
        arrayEven = convertToStringArray(arrayEventBuilder);

        return arrayEven;
    }

    // Из StringBuilder[] в String[]
    public static String[] convertToStringArray(StringBuilder[] arrayEvent) {
        String[] outputString = new String[arrayEvent.length];
        for (int i = 0; i < arrayEvent.length; i++) {
            outputString[i] = arrayEvent[i].toString();
        }

        return outputString;
    }

}
