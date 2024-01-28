package resources;

import DAO.NotesDAO;
import Messaging.NotesMessageListener;
import models.NoteCategories;
import models.Notes;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.websocket.Session;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ManagedBean
@SessionScoped
@Path("/notes")
public class NotesResource {

    private final NotesDAO notesDAO = new NotesDAO();
    private int selectedCategoryId;

    public int getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public void setSelectedCategoryId(int selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
    }
    private Notes newNote;
    private NoteCategories newNoteCategory;
    private int selectedNoteId;
    private List<Notes> allNotes;

    public int getSelectedNoteId() {
        return selectedNoteId;
    }

    public void setSelectedNoteId(int selectedNoteId) {
        this.selectedNoteId = selectedNoteId;
    }

    public NotesResource() {
        newNote = new Notes();
        newNoteCategory = new NoteCategories();
        editNote = new Notes();
        //registerMessageListener();
    }

    public Notes getNewNote() {
        return newNote;
    }

    public void setNewNote(Notes newNote) {
        this.newNote = newNote;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Notes> getAllNotes() {
        return notesDAO.getAllNotes();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addNote() {
        newNoteCategory = new NoteCategories();
        newNote.setCategory(newNoteCategory);

        // Set the selected category ID to the newNote
        newNote.getCategory().setCategoryId(selectedCategoryId);

        notesDAO.addNote(newNote);

        // Clear the newNote for the next entry
        newNote = new Notes();
        newNote.setCategory(new NoteCategories());

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addNoteCategory() {
        notesDAO.addNoteCategory(newNoteCategory);
        newNoteCategory = new NoteCategories();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<NoteCategories> getAllNoteCategories() {
        return notesDAO.getAllNoteCategories();
    }

    private Notes editNote;

    public Notes getEditNote() {
        return editNote;
    }

    public void setEditNote(Notes editNote) {
        this.editNote = editNote;
    }

    public List<SelectItem> getNoteIdList() {
        return notesDAO.getAllNoteIds();
    }

    public void loadNote() {
        editNote = notesDAO.getNoteById(selectedNoteId);
    }

    public void updateNote() {
        notesDAO.updateNoteById(selectedNoteId, editNote);
        // Reset values after updating
        editNote = new Notes();

        selectedNoteId = 0;

    }

    @DELETE
    @Path("/{noteId}")
    public Response deleteNote(@PathParam("noteId") int noteId) {
        notesDAO.deleteNoteById(noteId);
        return Response.noContent().build();
    }
    public NoteCategories getNewNoteCategory() {
        return newNoteCategory;
    }

    public void setNewNoteCategory(NoteCategories newNoteCategory) {
        this.newNoteCategory = newNoteCategory;
    }

    public void deleteCategoryById(int categoryId) {
        notesDAO.deleteCategoryById(categoryId);
    }

//    @Inject
//    private JMSContext context;
//
//    @Resource(lookup = "java:/jms/queue/NotesQueue")
//    private Queue notesQueue;
//
//    @Inject
//    private NotesMessageListener messageListener;
//
//    // Метод для реєстрації слухача
//    private void registerMessageListener() {
//        context.createConsumer(notesQueue).setMessageListener(messageListener);
//    }
}