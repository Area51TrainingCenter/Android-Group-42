package pe.area51.notepad;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

import pe.area51.notepad.ui.content.ViewModelContent;
import pe.area51.notepad.ui.list.ViewModelList;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final NotesRepository notesRepository;
    private final Executor workerThreadExecutor;
    private final Executor uiThreadExecutor;

    public ViewModelFactory(NotesRepository notesRepository,
                            Executor workerThreadExecutor,
                            Executor uiThreadExecutor) {
        this.workerThreadExecutor = workerThreadExecutor;
        this.uiThreadExecutor = uiThreadExecutor;
        this.notesRepository = notesRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ViewModelList.class)) {
            return (T) new ViewModelList(notesRepository, workerThreadExecutor, uiThreadExecutor);
        }
        if (modelClass.isAssignableFrom(ViewModelContent.class)) {
            return (T) new ViewModelContent(notesRepository, workerThreadExecutor, uiThreadExecutor);
        }
        return null;
    }

}
