package models;


import java.util.Date;

public class Notes {
    private int noteId;
    private String title;
    private String content;
    private Date createdat;
    private int notecategoryid;
    private String categoryName;  // Додайте це поле
    private NoteCategories category;  // Замінено int notecategoryid на NoteCategories category

    // ... інші геттери та сеттери

    public NoteCategories getCategory() {
        return category;
    }

    public void setCategory(NoteCategories category) {
        this.category = category;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Date createdat) {
        this.createdat = createdat;
    }

    public int getNotecategoryid() {
        return notecategoryid;
    }

    public void setNotecategoryid(int notecategoryid) {
        this.notecategoryid = notecategoryid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
