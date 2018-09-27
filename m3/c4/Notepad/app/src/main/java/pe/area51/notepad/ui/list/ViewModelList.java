package pe.area51.notepad.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import pe.area51.notepad.Note;
import pe.area51.notepad.NotesRepository;

public class ViewModelList extends ViewModel {

    private final NotesRepository notesRepository;
    private final Executor workerThreadExecutor;
    private final Executor uiThreadExecutor;

    private final MutableLiveData<LiveData<List<Note>>> liveDataNotes;
    private final MutableLiveData<Note> liveDataCreatedNote;

    public ViewModelList(NotesRepository notesRepository,
                         Executor workerThreadExecutor,
                         Executor uiThreadExecutor) {
        this.workerThreadExecutor = workerThreadExecutor;
        this.uiThreadExecutor = uiThreadExecutor;
        this.notesRepository = notesRepository;
        liveDataNotes = new MutableLiveData<>();
        liveDataCreatedNote = new MediatorLiveData<>();
    }

    public void fetchNotes() {
        workerThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final LiveData<List<Note>> notes = notesRepository.subscribeNotes();
                liveDataNotes.postValue(notes);
            }
        });
    }

    public void createEmptyNote() {
        workerThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final Note note = new Note(
                        "",
                        "",
                        System.currentTimeMillis()
                );
                liveDataCreatedNote.postValue(notesRepository.insertNote(note));
            }
        });
    }

    public LiveData<LiveData<List<Note>>> getLiveDataNotes() {
        return liveDataNotes;
    }

    public MutableLiveData<Note> getLiveDataCreatedNote() {
        return liveDataCreatedNote;
    }
}
