import io.*;
import processing.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String inputPath = "OTDRs/Modified OTDR.sor";
        String fileContent = FileReader.sorReader(inputPath);

        String outputPath = "OTDRs/Default.txt";
        FileSaver saveData = new FileSaver(outputPath);

        if (fileContent != null) {
            String fxdParameters = HexProcessing.extractParameters(fileContent);
            System.out.println(fxdParameters);
            String[] eventContent = HexProcessing.extractEvents(fileContent);
            saveData.writeContent(eventContent);
        } else {
            System.out.println("Файл не найден или не удалось прочитать файл");
        }
    }

}