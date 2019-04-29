package Client.Modals;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

public class RFC {

    private int number;
    private String title;

    public RFC(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileContent() {
        StringBuilder builder = new StringBuilder("");
        try {
            FileReader fr = new FileReader("RFCs/" + number + ".txt");

            int i;
            while ((i=fr.read()) != -1)
                builder.append((char) i);
            return builder.toString();
        } catch (Exception e){
            e.printStackTrace();
        }

        return "Error reading file data.";
    }

    public void setFileContent(String content) {
        try {
            Files.write(Paths.get("RFCs/" + number + "_new.txt"), content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(content);
    }

    public String getLastModified() {
        File file = new File("RFCs/" + number + ".txt");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return sdf.format(file.lastModified());
    }

    public long getFileSize() {
        File file = new File("RFCs/" + number + ".txt");
        return file.length();
    }
}
