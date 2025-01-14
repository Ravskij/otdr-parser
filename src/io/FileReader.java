package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    // Чтение данных из бинарного файла и запись содержимого в строку
    public static String sorReader(String path) {
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            StringBuilder fileContent = new StringBuilder();
            while (fileInputStream.available() != 0) {
                // Данные в файле записаны побайтово: 1 байт - 2 символа в 16-ричной системе.
                fileContent.append(String.format("%02X", fileInputStream.read()));
            }
            fileInputStream.close();
            return fileContent.toString();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static String[] folderFiles(String path) {
        List<String> sorFileList = new ArrayList<>();
        File folder = new File(path);
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.getName().contains(".sor")) {
                    sorFileList.add(file.getName());
                }
            }

            return sorFileList.toArray(new String[0]);
        } else {
            System.out.println("Введено имя файла, а не путь к папке");

            return new String[]{path};
        }
    }

}