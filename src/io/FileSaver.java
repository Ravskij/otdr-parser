package io;

import data.KeyEvents;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSaver {

    private String filePath;

    public FileSaver(String filePath) {
        this.filePath = filePath;
        createFile(filePath);
    }

    public void writeInCSV(List<KeyEvents> keyEvents, String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.append("\"Number\";" +
                    "\"Distance, km\";" +
                    "\"Attenuation coef, dB/km\";" +
                    "\"Attenuation in conn, dB\";" +
                    "\"Reflection coef, dB\"\n");
            for (KeyEvents event : keyEvents) {
                writer.append("\"").append(String.valueOf(event.getNumber())).append("\";");
                writer.append("\"").append(String.valueOf(event.getDistance())).append("\";");
                writer.append("\"").append(String.valueOf(event.getAttenuationCoefficient())).append("\";");
                writer.append("\"").append(String.valueOf(event.getAttenuationInConnection())).append("\";");
                writer.append("\"").append(String.valueOf(event.getReflectionCoefficient())).append("\"\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createFile(String filePath) {
        File file = new File(filePath);
        try {
            if (file.exists()) {
                System.out.println("Файл с именем " + filePath.substring(filePath.lastIndexOf("\\") + 1) +
                        " уже существует. Содержимое будет перезаписано");
            } else {
                if (file.createNewFile()) {
                    System.out.println("Файл с именем " + filePath.substring(filePath.lastIndexOf("\\") + 1) +
                            " создан");
                } else {
                    System.out.println("Ошибка при создании файла");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createDirectory(Path path) {
        if (Files.exists(path)) {
            System.out.println("Папка уже существует");
        } else {
            try {
                Files.createDirectories(path);
                System.out.println("Папка создана");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}