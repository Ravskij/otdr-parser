package processing;

public final class Constants {

    public static final int START_BLOCK = 18; // Смещение до "s" в "KeyEvents" или "FxdParams"
    public static final int KEY_EVENTS_OFFSET = 86; // Минимальное расстояние между событиями
    public static final int ONE_BYTE = 2; // Пропуск или считывание двух символов (1 байт)
    public static final int SHORT_INT_HEX = 4; // Число символов, соответствующее short int (2 байта) в hex формате
    public static final int LONG_INT_HEX = 8; // Число символов, соответствующее long int (4 байта) в hex формате
    public static final double SPEED_OF_LIGHT = 299_792_458; // Скорость света в вакууме, м/с
    public static final double REFRACTION_INDEX = 1.4682; // Коэффициент преломления оптического волокна

}
