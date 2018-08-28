package pe.area51.notepad.data.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import pe.area51.notepad.Note;
import pe.area51.notepad.NotesRepository;

public class NotesSqLiteRepository implements NotesRepository {

    private final NotesSqLiteDatabase notesSqLiteDatabase;

    public NotesSqLiteRepository(NotesSqLiteDatabase notesSqLiteDatabase) {
        this.notesSqLiteDatabase = notesSqLiteDatabase;
    }

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

    @Override
    public Note getNoteByTitle(String title) {
        return null;
    }

    @Override
    public boolean insertNote(Note note) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put("title", note.getTitle());
        contentValues.put("content", note.getContent());
        contentValues.put("creationTimestamp", note.getCreationTimestamp());
        return notesSqLiteDatabase.getWritableDatabase().insert("notes", null, contentValues) != -1;
    }

    @Override
    public boolean updateNote(Note oldNote, Note newNote) {
        return false;
    }

    @Override
    public boolean deleteNoteByTitle(String noteTitle) {
        return false;
    }
}
