package Client.Modals;

public class RFC {

    private int number;
    private String title;

    public RFC(int number){
        this.number = number;
    }

    public RFC(int number, String content){
        this.number = number;
        setFileContent(content);
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

    public String getFileContent(){
        //TODO read content from file
        return "This is the  content of an RFC file";
    }

    public void setFileContent(String content){
        //TODO write content to  file
    }
}
