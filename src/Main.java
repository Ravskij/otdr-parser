import data.FxdParams;
import io.*;

import data.KeyEvents;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import static io.FileReader.*;
import static io.FileSaver.*;
import static processing.HexProcessing.*;

public class Main {

    public static void main(String[] args) {
        String path; // Путь к папке с файлами .sor
        String inputPath; // Путь до .sor-файла
        String outputPath; // Путь вывода данных (..\output\.. .csv
        String fileContent; // Содержимое .sor-файла
        FileSaver saveData; // Для сохранения данных в .csv
        List<KeyEvents> eventContent; // Содержимое KeyEvent блока
        FxdParams fxdParamContent; // Содержимое FxdParams блока

        Scanner scanner = new Scanner(System.in);
        // Console console = System.console();
        System.out.print("Введите путь к файлу (чтение): ");
        // path = console.readLine();
        path = scanner.nextLine();

        String[] sorFiles = sorInDirectory(path);
        if (sorFiles.length != 0) {
            outputPath = path + "\\" + "output";
            createDirectory(Path.of(outputPath));
            for (String file : sorFiles) {
                inputPath = path + "\\" + file;
                fileContent = FileReader.sorReader(inputPath);
                outputPath = path + "\\" + "output" + "\\" + file.substring(0, file.lastIndexOf(".sor")) + ".csv";
                saveData = new FileSaver(outputPath);

                if (fileContent != null) {
                    fxdParamContent = extractParameters(fileContent);
                    System.out.println(fxdParamContent);
                    eventContent = extractEvents(fileContent);
                    saveData.writeInCSV(eventContent, outputPath);
                } else {
                    System.out.println("Файл не найден или не удалось прочитать файл");
                }
            }
        } else {
            System.out.println("В директории нет файлов с расширением .sor");
        }
    }

}