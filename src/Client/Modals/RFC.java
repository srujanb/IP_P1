package Client.Modals;

import Utils.CommonConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        StringBuilder builder;
        try {
            builder = new StringBuilder("");
            File file = new File("RFCs/" + number + ".txt");

            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null)
                builder.append(st);
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error reading file data.";
    }

    public void setFileContent(String content) {
        try {
            Files.write(Paths.get("NEWRFCs/" + number + ".txt"), content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
