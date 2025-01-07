import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String filePath = "OTDRs/Modified OTDR.sor";
        String fileContent = OpenSor.sorReader(filePath);

        SaveData saveData = new SaveData();

        if (fileContent != null) {
            String[] eventContent = EventProcessing.processEvents(fileContent);
            saveData.saveToFile(eventContent);
        } else {
            System.out.println("Файл не найден или не удалось прочитать файл");
        }
    }
}