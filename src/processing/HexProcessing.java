package processing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import data.FxdParams;
import data.KeyEvents;
import static processing.HexKit.*;
import static processing.Constants.*;

public class HexProcessing {

    static int cursor; // Курсор для перемещения по бинарному файлу

    // Обработка блока FxdParams
    public static FxdParams extractParameters(String hexContent) {

        FxdParams fxdParams = new FxdParams();

        // Преобразование строки "FxdParams" к определенному виду для дальнейшего поиска блока FxdParams по файлу
        cursor = hexContent.lastIndexOf(HexKit.compareToHex("FxdParams")) + START_BLOCK + ONE_BYTE;
        fxdParams.setDate(new Date(HexKit.compareToDec(HexKit.hexScanner(hexContent, cursor, LONG_INT_HEX))));
        cursor += LONG_INT_HEX;
        fxdParams.setDistanceUnits("" + (char)HexKit.compareToDec(HexKit.hexScanner(hexContent, cursor, ONE_BYTE)) +
                (char)HexKit.compareToDec(HexKit.hexScanner(hexContent, cursor + ONE_BYTE, ONE_BYTE)));
        cursor += ONE_BYTE + ONE_BYTE;
        fxdParams.setWavelength(HexKit.compareToDec(HexKit.flipHex(HexKit.hexScanner(hexContent, cursor, SHORT_INT_HEX))) / 10);
        cursor += SHORT_INT_HEX;

        return fxdParams;
    }

    // Обработка блока KeyEvents
    public static List<KeyEvents> extractEvents(String hexContent) {

        List<KeyEvents> keyEvents = new ArrayList<>();

        int countEvents; // Общее количество событий в блоке KeyEvents
        int eventCursor; // Курсор начала каждого события
        double transitTime; // Время распространения импульса в с
        double reflectionCoefficient;

        String hexNumber; // Десятичное число, приведенное к шестнадцатеричному

        // Преобразование строки "KeyEvents" к определенному виду для дальнейшего поиска блока KeyEvents по файлу
        eventCursor = hexContent.lastIndexOf(compareToHex("KeyEvents")) + START_BLOCK + ONE_BYTE;
        countEvents = (int) compareToDec(hexScanner(hexContent, eventCursor, ONE_BYTE));

        // Первое событие находится около курсора, перемещаем его на безопасное расстояние
        eventCursor -= KEY_EVENTS_OFFSET;
        for (int i = 0; i < countEvents; i++) {
            KeyEvents event = new KeyEvents();
            hexNumber = compareToHex(i + 1, SHORT_INT_HEX);
            eventCursor = cursor = hexContent.indexOf(hexNumber, eventCursor + KEY_EVENTS_OFFSET);
            if (i != 0) { // первое(нулевое) событие пропускается
                event.setNumber(i); // Порядковый номер
                cursor += SHORT_INT_HEX;
                transitTime = compareToDec(flipHex(hexScanner(hexContent, cursor + ONE_BYTE, LONG_INT_HEX))) * Math.pow(10, -12); // Время распространения
                event.setDistance((double) (Math.round(((transitTime * SPEED_OF_LIGHT) / REFRACTION_COEFFICIENT) * 1000)) / 10000); // Расстояние до события
                cursor += LONG_INT_HEX;
                event.setAttenuationCoefficient((double) compareToDec(hexScanner(hexContent, cursor, SHORT_INT_HEX)) / 1000); // Километрическое затухание
                cursor += SHORT_INT_HEX + ONE_BYTE;
                if (hexScanner(hexContent, cursor + ONE_BYTE, ONE_BYTE).equals("FF")) { // Затухание на событии
                    event.setAttenuationInConnection(-(double) compareToDec(invertHex(flipHex(hexScanner(hexContent, cursor, SHORT_INT_HEX) + "FFFF"))) / 1000);
                } else {
                    event.setAttenuationInConnection((double) compareToDec(flipHex(hexScanner(hexContent, cursor, SHORT_INT_HEX))) / 1000);
                }
                cursor += SHORT_INT_HEX;
                reflectionCoefficient = -(double) compareToDec(invertHex(flipHex(hexScanner(hexContent, cursor, LONG_INT_HEX)))) / 1000; // Коэффициент отражения
                event.setReflectionCoefficient(reflectionCoefficient);
            } else { // Отдельное описание для первого события
                event.setNumber(0); // Порядковый номер
                event.setDistance(0.0000); // Расстояние
                event.setAttenuationCoefficient(0.0000); // Не учитывается для первого(нулевого) события
                event.setAttenuationInConnection(0.0000); // Не учитывается для первого(нулевого) события
                event.setReflectionCoefficient(0.0000); // Не учитывается для первого(нулевого) события
            }
            keyEvents.add(event);
        }
        System.out.println("Всего зафиксированных событий: " + countEvents);

        return keyEvents;
    }

}