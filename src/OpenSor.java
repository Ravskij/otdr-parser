import java.io.FileInputStream;
import java.io.IOException;

public class OpenSor {
    // Чтение данных из бинарного файла и запись содержимого в строку
    public static String sorReader(String path) {
        // Данные в файле записаны побайтово: 1 байт - 2 символа в 16-ричной системе.
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            StringBuilder fileContent = new StringBuilder();
            while (fileInputStream.available() != 0) {
                fileContent.append(String.format("%02X", fileInputStream.read()));
            }
            fileInputStream.close();
            return fileContent.toString();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}