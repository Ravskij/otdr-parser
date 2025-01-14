import io.*;
import processing.*;

import java.io.Console;
import java.util.Scanner;
import java.io.IOException;
import static io.FileReader.*;
import static io.FileSaver.*;

public class Main {

    public static void main(String[] args) throws IOException {
        String path; // Путь к папке с файлами .sor
        String inputPath; // Путь ввода данных
        String outputPath; // Путь вывода данных
        String fileContent; // Все данные из .sor-файла
        FileSaver saveData; // Объект класса FileSaver для сохранения данных

        // Console console = System.console();
        System.out.print("Введите путь к файлу (чтение): ");
        // path = console.readLine();
        Scanner scanner = new Scanner(System.in);
        path = scanner.nextLine();
        // path = path.replace("\\", "\\\\");

        String[] sorFiles = folderFiles(path);
        outputPath = path + "\\" + "output";
        isExist(outputPath);
        for (String file : sorFiles) {
            inputPath = path + "\\" + file;
            fileContent = FileReader.sorReader(inputPath);
            outputPath = path + "\\" + "output" + "\\" + file.substring(0, file.lastIndexOf(".sor")) + ".txt";
            saveData = new FileSaver(outputPath);

            if (fileContent != null) {
                String fxdParameters = HexProcessing.extractParameters(fileContent);
                System.out.println(fxdParameters);
                String[] eventContent = HexProcessing.extractEvents(fileContent);
                saveData.writeContent(eventContent); // проверку есть ли event content
            } else {
                System.out.println("Файл не найден или не удалось прочитать файл");
            }
        }
    }

}