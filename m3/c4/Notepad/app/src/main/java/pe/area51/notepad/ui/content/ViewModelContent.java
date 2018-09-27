package pe.area51.notepad.ui.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pe.area51.notepad.Note;
import pe.area51.notepad.NotesRepository;

public class ViewModelContent extends ViewModel {

    private final NotesRepository notesRepository;
    private Executor workerThreadExecutor;
    private Executor uiThreadExecutor;

    private final MutableLiveData<Note> liveDataNote;
    private final MutableLiveData<Boolean> liveDataIsNoteDeleted;

    public ViewModelContent(NotesRepository notesRepository,
                            Executor workerThreadExecutor,
                            Executor uiThreadExecutor) {
        this.workerThreadExecutor = workerThreadExecutor;
        this.uiThreadExecutor = uiThreadExecutor;
        this.notesRepository = notesRepository;
        this.liveDataNote = new MutableLiveData<>();
        this.liveDataIsNoteDeleted = new MutableLiveData<>();
    }

    public void deleteNote(final String id) {
        workerThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                notesRepository.deleteNoteById(id);
                liveDataIsNoteDeleted.postValue(true);
            }
        });
    }

    public void fetchNote(final String id) {
        workerThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final Note note = notesRepository.getNoteById(id);
                liveDataNote.postValue(note);
            }
        });
    }

    public void updateNote(final Note note) {
        workerThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                notesRepository.updateNote(note);
            }
        });
    }

    public LiveData<Note> getLiveDataNote() {
        return liveDataNote;
    }

    public LiveData<Boolean> getLiveDataIsNoteDeleted() {
        return liveDataIsNoteDeleted;
    }
}
