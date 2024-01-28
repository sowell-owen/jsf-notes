package DAO;

import models.NoteCategories;
import models.Notes;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "notesDAO", eager = true)
@SessionScoped
public class NotesDAO implements Serializable {

    public Connection getConnection() {
        Connection con = null;

        try {
            String url = "jdbc:postgresql://localhost/NotesDB";
            String user = "postgres";
            String password = "root";
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Connection completed.");

        } catch (SQLException ex) {
            System.out.println("1841984714897149814791471984174981741981748917987194174891 ERR: " + ex.getMessage());
        } finally {
        }

        return con;

    }

    public List<NoteCategories> getAllNoteCategories() {
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = getConnection();
        String stm = "SELECT * FROM notecategories";
        List<NoteCategories> categoriesList = new ArrayList<>();

        try {
            pst = con.prepareStatement(stm);
            pst.execute();
            rs = pst.getResultSet();

            while (rs.next()) {
                NoteCategories category = new NoteCategories();
                category.setCategoryId(rs.getInt("categoryid"));
                category.setName(rs.getString("name"));
                categoriesList.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("EEE: " + e.getMessage());
        } finally {
            //   closeResources(pst, con);
        }
        return categoriesList;
    }

    public List<Notes> getAllNotes() {
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = getConnection();
        String stm = "SELECT n.*, c.name as categoryName FROM notes n INNER JOIN notecategories c ON n.notecategoryid = c.categoryid";
        List<Notes> records = new ArrayList<>();

        try {
            pst = con.prepareStatement(stm);
            pst.execute();
            rs = pst.getResultSet();

            while (rs.next()) {
                Notes note = new Notes();
                note.setNoteId(rs.getInt("noteid"));
                note.setTitle(rs.getString("title"));
                note.setContent(rs.getString("content"));
                note.setCreatedat(rs.getDate("createdat"));

                // Отримайте назву категорії з результату запиту
                note.setCategoryName(rs.getString("categoryName"));

                records.add(note);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("EEE: " + e.getMessage());
        } finally {
            //  closeResources(pst, con);
        }
        return records;
    }

    public NoteCategories getNoteCategoryById(int categoryId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        NoteCategories category = null;

        try {
            con = getConnection();
            String stm = "SELECT * FROM notecategories WHERE categoryid = ?";
            pst = con.prepareStatement(stm);
            pst.setInt(1, categoryId);
            rs = pst.executeQuery();

            if (rs.next()) {
                category = new NoteCategories();
                category.setCategoryId(rs.getInt("categoryid"));
                category.setName(rs.getString("name"));
                // Додайте інші властивості, якщо вони є
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error getting note category by id: " + e.getMessage());
        } finally {
            //  closeResources(pst, con);
        }

        return category;
    }

    public void addNote(Notes note) {
        Connection con = null;
        PreparedStatement pst = null;
        String url = "jdbc:postgresql://localhost/NotesDB";
        String user = "postgres";
        String password = "root";

        try {
            con = DriverManager.getConnection(url, user, password);

            String stm = "INSERT INTO notes (title, content, createdat, notecategoryid) VALUES (?, ?, ?, ?)";
            pst = con.prepareStatement(stm);
            pst.setString(1, note.getTitle());
            pst.setString(2, note.getContent());
            pst.setDate(3, new java.sql.Date(note.getCreatedat().getTime()));
            pst.setInt(4, note.getCategory().getCategoryId());

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding note: " + e.getMessage());
        } finally {
            // closeResources(pst, con);
        }
    }

    public void updateNote(Notes note) {
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = getConnection();
            String stm = "UPDATE notes SET title=?, content=?, createdat=?, notecategoryid=? WHERE noteid=?";
            pst = con.prepareStatement(stm);
            pst.setString(1, note.getTitle());
            pst.setString(2, note.getContent());
            pst.setDate(3, new java.sql.Date(note.getCreatedat().getTime()));
            pst.setInt(4, note.getNotecategoryid());
            pst.setInt(5, note.getNoteId());

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating note: " + e.getMessage());
        } finally {
            //  closeResources(pst, con);
        }
    }

    public void updateNoteById(int id, Notes updatedNote) {
        Connection con = null;
        PreparedStatement pst = null;

        String url = "jdbc:postgresql://localhost/NotesDB";
        String user = "postgres";
        String password = "root";

        try {
            con = DriverManager.getConnection(url, user, password);

            String stm = "UPDATE notes SET title = ?, content = ?, createdat = ?, notecategoryid = ? WHERE noteid = ?";
            pst = con.prepareStatement(stm);
            pst.setString(1, updatedNote.getTitle());
            pst.setString(2, updatedNote.getContent());
            pst.setDate(3, new java.sql.Date(updatedNote.getCreatedat().getTime()));
            pst.setInt(4, 1);
            pst.setInt(5, id);

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating note: " + e.getMessage());
        } finally {
            // closeResources(pst, con);
        }
    }


    public List<SelectItem> getAllNoteIds() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        List<SelectItem> noteIdList = new ArrayList<>();

        try {
            con = getConnection();
            String stm = "SELECT noteid FROM notes";
            pst = con.prepareStatement(stm);
            rs = pst.executeQuery();

            while (rs.next()) {
                int noteId = rs.getInt("noteid");
                noteIdList.add(new SelectItem(noteId, String.valueOf(noteId)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error getting note ids: " + e.getMessage());
        } finally {
            // closeResources(pst, con);
        }

        return noteIdList;
    }

    public Notes getNoteById(int noteId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Notes note = null;

        try {
            con = getConnection();
            String stm = "SELECT * FROM notes WHERE noteid = ?";
            pst = con.prepareStatement(stm);
            pst.setInt(1, noteId);
            rs = pst.executeQuery();

            if (rs.next()) {
                note = new Notes();
                note.setNoteId(rs.getInt("noteid"));
                note.setTitle(rs.getString("title"));
                note.setContent(rs.getString("content"));
                note.setCreatedat(rs.getDate("createdat"));
                // Add more properties as needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error getting note by id: " + e.getMessage());
        } finally {
        }

        return note;
    }

    public void deleteNoteById(int id) {
        Connection con = null;
        PreparedStatement pst = null;

        String url = "jdbc:postgresql://localhost/NotesDB";
        String user = "postgres";
        String password = "root";

        try {
            con = DriverManager.getConnection(url, user, password);

            String stm = "DELETE FROM notes WHERE noteid = ?";
            pst = con.prepareStatement(stm);
            pst.setInt(1, id);

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting note: " + e.getMessage());
        } finally {
            //closeResources(pst, con);
        }
    }

    public void addNoteCategory(NoteCategories category) {
        Connection con = null;
        PreparedStatement pst = null;
        String url = "jdbc:postgresql://localhost/NotesDB";
        String user = "postgres";
        String password = "root";

        try {
            con = DriverManager.getConnection(url, user, password);

            String stm = "INSERT INTO notecategories (name) VALUES (?)";
            pst = con.prepareStatement(stm);
            pst.setString(1, category.getName());

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding note category: " + e.getMessage());
        } finally {
            //closeResources(pst, con);
        }
    }

    public void deleteCategoryById(int categoryId) {
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = getConnection();
            String stm = "DELETE FROM notecategories WHERE categoryid = ?";
            pst = con.prepareStatement(stm);
            pst.setInt(1, categoryId);

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting category: " + e.getMessage());
        } finally {
            // Close resources (pst, con) if needed
        }
    }


//    private void closeResources(PreparedStatement pst, Connection con) {
//        try {
//            if (pst != null) {
//                pst.close();
//            }
//            if (con != null) {
//                con.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Error closing resources: " + e.getMessage());
//        }
//    }
}