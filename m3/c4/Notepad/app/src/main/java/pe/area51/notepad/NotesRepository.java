package pe.area51.notepad;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public interface NotesRepository {

    @NonNull
    LiveData<List<Note>> subscribeNotes();

    @NonNull
    List<Note> getNotes();

    @NonNull
    Note getNoteById(@NonNull String id);

    @NonNull
    Note insertNote(@NonNull Note note);

    boolean updateNote(@NonNull Note note);

    boolean deleteNoteById(@NonNull String noteId);

}
