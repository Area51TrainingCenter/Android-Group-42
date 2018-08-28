package pe.area51.notepad;

import android.arch.persistence.room.Room;

import pe.area51.notepad.data.room.NotesRoomDatabase;
import pe.area51.notepad.data.room.NotesRoomRepository;

public class Application extends android.app.Application {

    private NotesRepository notesRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        /*final NotesSqLiteDatabase notesSqLiteDatabase = new NotesSqLiteDatabase(this, "notesSqLiteDatabase");
        notesRepository = new NotesSqLiteRepository(notesSqLiteDatabase);*/

        final NotesRoomDatabase notesRoomDatabase = Room
                .databaseBuilder(this, NotesRoomDatabase.class, "notesRoomDatabase")
                .build();
        notesRepository = new NotesRoomRepository(notesRoomDatabase);
    }

    public NotesRepository getNotesRepository() {
        return notesRepository;
    }
}
