package pe.area51.githublist.ui.repository_list;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

import pe.area51.githublist.domain.GitHubRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final GitHubRepository gitHubRepository;
    private final Executor workerThreadExecutor;
    private final Executor uiThreadExecutor;

    public ViewModelFactory(GitHubRepository gitHubRepository, Executor workerThreadExecutor, Executor uiThreadExecutor) {
        this.gitHubRepository = gitHubRepository;
        this.workerThreadExecutor = workerThreadExecutor;
        this.uiThreadExecutor = uiThreadExecutor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ViewModelGitHubRepositoryList.class)) {
            return (T) new ViewModelGitHubRepositoryList(gitHubRepository, workerThreadExecutor, uiThreadExecutor);
        }
        throw new RuntimeException("Unknown ViewModel class!");
    }
}
