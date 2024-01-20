package library.ck_librarymanagament;

public class library {
    private int ID;
    private String namebook;
    private String author;
    private String title;
    private String describe;

    // Constructor
    public library(int ID, String namebook, String author, String title, String describe) {
        this.ID = ID;
        this.namebook = namebook;
        this.author = author;
        this.title = title;
        this.describe = describe;
    }

    public int getID() {
        return ID;
    }

    public void setId(int id) {
        this.ID = id;
    }

    public String getNamebook() {
        return namebook;
    }

    public void setNamebook(String namebook) {
        this.namebook = namebook;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
