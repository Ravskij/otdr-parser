import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileSaver {

    private String filePath;

    public FileSaver() throws IOException {
        this.filePath = "Default.txt";
        createFile(filePath);
    }

    public FileSaver(String filePath) throws IOException{
        this.filePath = filePath;
        createFile(filePath);
    }

    public void writeContent(String[] content) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.append("Номер\t" +
                    "Расстояние, км\t" +
                    "Коэффициент затухания, дБ/км\t" +
                    "Затухание в соединении, дБ\t" +
                    "Коэффициент отражения, дБ\t").append("\n");
            for (int i = 0; i < content.length; i++) {
                fileWriter.append(content[i]).append("\n");
            }
            fileWriter.flush();
        }
    }

    public void createFile(String filePath) throws IOException{
        File file = new File(filePath);
        if (file.exists()) {
            System.out.println("Файл с именем " + filePath.substring(filePath.lastIndexOf("/") + 1) + " уже существует");
        } else {
            if (file.createNewFile()) {
                System.out.println("Файл создан");
            } else {
                System.out.println("Ошибка при создании файла");
            }
        }
    }

}