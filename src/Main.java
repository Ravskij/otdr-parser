import data.FxdParams;
import io.*;

import data.KeyEvents;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import static io.FileSaver.*;
import static processing.HexProcessing.*;

public class Main {

    public static void main(String[] args) {

        int num;
        String onePath;
        String path; // Путь к папке с файлами .sor
        String inputPath; // Путь до .sor-файла
        String outputPath; // Путь вывода данных \..\output\.. .csv
        String fileContent; // Содержимое .sor-файла
        String[] sorFiles; // Список файлов .sor в выбранном каталоге
        FileSaver saveData; // Сохранение данных в .csv
        FileReader fileReader; // Определение файлов .sor в каталоге и чтение данных из них
        List<KeyEvents> eventContent; // Содержимое KeyEvent блока
        FxdParams fxdParamContent; // Содержимое FxdParams блока

        Scanner scanner = new Scanner(System.in);
        // Console console = System.console();
        System.out.print("Введите путь к файлу (чтение): ");
        // path = console.readLine();
        path = scanner.nextLine();
        fileReader = new FileReader();
        sorFiles = fileReader.sorInDirectory(path);
        if (sorFiles.length != 0) {
            fileReader.fiberNumberWidth(sorFiles);
            outputPath = path + "\\" + "output";
            onePath = outputPath + "\\" + "ONE.csv";
            createDirectory(Path.of(outputPath));
            for (String file : sorFiles) {
                inputPath = path + "\\" + file;
                fileContent = fileReader.sorReader(inputPath);
                //num = fileReader.fiberNumber(file);
                //System.out.println(num);
                outputPath = path + "\\" + "output" + "\\" + file.substring(0, file.lastIndexOf(".sor")) + ".csv";
                saveData = new FileSaver(outputPath);
                if (fileContent != null) {
                    fxdParamContent = extractParameters(fileContent);
                    System.out.println(fxdParamContent);
                    eventContent = extractEvents(fileContent);
                    FileSaver.writeInOneCSV(eventContent, onePath);
                    saveData.writeInCSV(eventContent);
                } else {
                    System.out.println("Файл не найден или не удалось прочитать файл");
                }
            }
        } else {
            System.out.println("В директории отсутствуют файлы с расширением .sor");
        }
    }

}