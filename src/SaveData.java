import java.io.FileWriter;
import java.io.IOException;

public class SaveData {
    private String fileName;
    private FileWriter fileWriter;

    public SaveData() {
        this.fileName = "Default";
        try {
            this.fileWriter = new FileWriter(fileName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public SaveData(String fileName) {
        try {
            this.fileWriter = new FileWriter(fileName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveToFile(String content) throws IOException {
        fileWriter.append(content).append("\n");
        fileWriter.flush();
    }
}