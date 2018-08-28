package pe.area51.notepad.data.room;

import java.util.ArrayList;
import java.util.List;

import pe.area51.notepad.Note;
import pe.area51.notepad.NotesRepository;

public class NotesRoomRepository implements NotesRepository {

    private final NotesRoomDatabase notesRoomDatabase;

    public NotesRoomRepository(NotesRoomDatabase notesRoomDatabase) {
        this.notesRoomDatabase = notesRoomDatabase;
    }

    @Override
    public List<Note> getNotes() {
        final List<pe.area51.notepad.data.room.Note> roomNotes = notesRoomDatabase.getNotesDao().getAll();
        final List<Note> notes = new ArrayList<>();
        for (final pe.area51.notepad.data.room.Note roomNote : roomNotes) {
            notes.add(
                    new Note(
                            roomNote.getTitle(),
                            roomNote.getContent(),
                            roomNote.getCreationTimestamp()
                    )
            );
        }
        return notes;
    }

    //TODO
    @Override
    public Note getNoteByTitle(String title) {
        return null;
    }

    @Override
    public boolean insertNote(Note note) {
        return notesRoomDatabase.getNotesDao().insert(
                new pe.area51.notepad.data.room.Note(
                        0,
                        note.getTitle(),
                        note.getContent(),
                        note.getCreationTimestamp()
                )
        ) != -1;
    }

    //TODO
    @Override
    public boolean updateNote(Note oldNote, Note newNote) {
        return false;
    }

    //TODO
    @Override
    public boolean deleteNoteByTitle(String noteTitle) {
        return false;
    }
}
