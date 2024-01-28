package net.proselyte.jspproject;

import DAO.NotesDAO;
import models.NoteCategories;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "noteCategoriesConverter")
public class NoteCategoriesConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                NotesDAO notesDAO = new NotesDAO();
                int categoryId = Integer.parseInt(value);
                return notesDAO.getNoteCategoryById(categoryId);
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid category ID."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
        if (object != null) {
            return String.valueOf(((NoteCategories) object).getCategoryId());
        } else {
            return null;
        }
    }
}
