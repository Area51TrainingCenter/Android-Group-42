package pe.area51.notepad.data.room;

import android.support.annotation.NonNull;

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
    @NonNull
    public List<Note> getNotes() {
        final List<pe.area51.notepad.data.room.Note> roomNotes = notesRoomDatabase.getNotesDao().getAll();
        final List<Note> notes = new ArrayList<>();
        for (final pe.area51.notepad.data.room.Note roomNote : roomNotes) {
            notes.add(roomNoteToEntityNote(roomNote));
        }
        return notes;
    }

    @NonNull
    @Override
    public Note getNoteById(@NonNull String id) {
        return roomNoteToEntityNote(notesRoomDatabase.getNotesDao().getById(Long.valueOf(id)));
    }

    @Override
    @NonNull
    public Note insertNote(@NonNull Note note) {
        final long noteId = notesRoomDatabase.getNotesDao().insert(
                new pe.area51.notepad.data.room.Note(
                        note.getTitle(),
                        note.getContent(),
                        note.getCreationTimestamp()
                )
        );
        return new Note(
                String.valueOf(noteId),
                note.getTitle(),
                note.getContent(),
                note.getCreationTimestamp()
        );
    }

    @Override
    public boolean updateNote(@NonNull Note note) {
        return notesRoomDatabase.getNotesDao().update(new pe.area51.notepad.data.room.Note(
                Long.valueOf(note.getId()),
                note.getTitle(),
                note.getContent(),
                note.getCreationTimestamp()
        )) == 1;
    }

    @Override
    public boolean deleteNoteById(@NonNull String noteId) {
        return notesRoomDatabase.getNotesDao().delete(new pe.area51.notepad.data.room.Note(Long.valueOf(noteId))) == 1;
    }

    private Note roomNoteToEntityNote(final pe.area51.notepad.data.room.Note roomNote) {
        return new Note(
                String.valueOf(roomNote.getId()),
                roomNote.getTitle(),
                roomNote.getContent(),
                roomNote.getCreationTimestamp()
        );
    }

}
