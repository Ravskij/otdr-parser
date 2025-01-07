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

    public void saveToFile(String[] content) throws IOException {
        for (int i = 0; i < content.length - 1; i++) {
            fileWriter.append(content[i]).append("\n");
            fileWriter.flush();
        }
    }
}