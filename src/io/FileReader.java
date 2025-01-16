package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    private int start = Integer.MAX_VALUE; // Стартовый номер символа в череде несовпадений названия файла
    private int end = Integer.MAX_VALUE; // Последний номер символа в череде несовпадений названия файла

    // Чтение данных из бинарного файла и запись содержимого в строку
    public String sorReader(String path) {
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

    // Возвращает массив строк с именами .sor
    public String[] sorInDirectory(String path) {
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

    // Определение границ различия в названии файла (номер волокна)
    public void fiberNumberWidth(String[] sorFiles) {

        boolean flag = false;
        int fileLength = sorFiles[0].length();

        for (int i = 0; i < sorFiles.length - 1; i++) {
            if (fileLength == sorFiles[i].length()) {
                for (int j = 0; j < fileLength; j++) {
                    if (sorFiles[i].charAt(j) != sorFiles[i + 1].charAt(j)) {
                        if (start > j) {
                            start = j;
                            flag = true;
                        }
                    } else {
                        if (flag && end > j) {
                            end = j;
                            flag = false;
                        }
                    }
                }
                flag = false;
            } else {
                throw new IllegalArgumentException("Различие в длине имен файлов");
            }
        }
    }

    // Определение номера волокна по имени файла с учетом ранее определенных границ различия в именах
    public int fiberNumber(String file) {
        return Integer.parseInt(file.substring(start, end));
    }

}