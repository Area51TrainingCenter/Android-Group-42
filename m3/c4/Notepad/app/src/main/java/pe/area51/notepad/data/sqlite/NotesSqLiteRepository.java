package pe.area51.notepad.data.sqlite;

import android.arch.lifecycle.LiveData;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import pe.area51.notepad.Note;
import pe.area51.notepad.NotesRepository;

public class NotesSqLiteRepository implements NotesRepository {

    private final NotesSqLiteDatabase notesSqLiteDatabase;

    public NotesSqLiteRepository(NotesSqLiteDatabase notesSqLiteDatabase) {
        this.notesSqLiteDatabase = notesSqLiteDatabase;
    }

    @NonNull
    @Override
    public LiveData<List<Note>> subscribeNotes() {
        throw new UnsupportedOperationException("Not implemented!");
    }

    @NonNull
    @Override
    public List<Note> getNotes() {
        final Cursor queryCursor = notesSqLiteDatabase.getReadableDatabase().rawQuery("SELECT * FROM notes", null);
        final List<Note> notes = new ArrayList<>();
        while (queryCursor.moveToNext()) {
            final String title = queryCursor.getString(queryCursor.getColumnIndex("title"));
            final String content = queryCursor.getString(queryCursor.getColumnIndex("content"));
            final long creationTimestamp = queryCursor.getLong(queryCursor.getColumnIndex("creationTimestamp"));
            final Note note = new Note(title, content, creationTimestamp);
            notes.add(note);
        }
        queryCursor.close();
        return notes;
    }

    @NonNull
    @Override
    public Note getNoteById(@NonNull String id) {
        throw new UnsupportedOperationException("Not implemented!");
    }

    @NonNull
    @Override
    public Note insertNote(@NonNull Note note) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put("title", note.getTitle());
        contentValues.put("content", note.getContent());
        contentValues.put("creationTimestamp", note.getCreationTimestamp());
        final long noteId = notesSqLiteDatabase.getWritableDatabase().insert("notes", null, contentValues);
        return new Note(
                String.valueOf(noteId),
                note.getTitle(),
                note.getContent(),
                note.getCreationTimestamp()
        );
    }

    @Override
    public boolean updateNote(@NonNull Note note) {
        throw new UnsupportedOperationException("Not implemented!");
    }

    @Override
    public boolean deleteNoteById(@NonNull String noteId) {
        throw new UnsupportedOperationException("Not implemented!");
    }
}
