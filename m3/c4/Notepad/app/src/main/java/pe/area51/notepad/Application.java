package pe.area51.notepad;

import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import pe.area51.notepad.data.room.NotesRoomDatabase;
import pe.area51.notepad.data.room.NotesRoomRepository;
import pe.area51.notepad.executors.DefaultThreadPoolExecutor;
import pe.area51.notepad.executors.UiThreadExecutor;

public class Application extends android.app.Application {

    private NotesRepository notesRepository;

    private ViewModelFactory viewModelFactory;

    private Executor workerThreadExecutor;
    private Executor uiThreadExecutor;

    @Override
    public void onCreate() {
        super.onCreate();

        /*final NotesSqLiteDatabase notesSqLiteDatabase = new NotesSqLiteDatabase(this, "notesSqLiteDatabase");
        notesRepository = new NotesSqLiteRepository(notesSqLiteDatabase);*/

        final NotesRoomDatabase notesRoomDatabase = Room
                .databaseBuilder(this, NotesRoomDatabase.class, "notesRoomDatabase")
                .build();
        workerThreadExecutor = new DefaultThreadPoolExecutor();
        uiThreadExecutor = new UiThreadExecutor();
        notesRepository = new NotesRoomRepository(notesRoomDatabase);
        viewModelFactory = new ViewModelFactory(
                notesRepository,
                workerThreadExecutor,
                uiThreadExecutor);
    }

    public NotesRepository getNotesRepository() {
        return notesRepository;
    }

    public ViewModelFactory getViewModelFactory() {
        return viewModelFactory;
    }
}
