package pe.area51.notepad;

import java.util.List;

public interface NotesRepository {

    List<Note> getNotes();

    Note getNoteByTitle(String title);

    boolean insertNote(Note note);

    boolean updateNote(Note oldNote, Note newNote);

    boolean deleteNoteByTitle(String noteTitle);

}
