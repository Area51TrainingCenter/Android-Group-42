package pe.area51.githublist.ui.repository_list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import pe.area51.githublist.R;
import pe.area51.githublist.domain.GitHubRepository;
import pe.area51.githublist.domain.GitHubRepositoryException;
import pe.area51.githublist.domain.Repository;
import pe.area51.githublist.domain.User;
import pe.area51.githublist.ui.Response;

public class ViewModelGitHubRepositoryList extends ViewModel {

    private final GitHubRepository gitHubRepository;
    private final Executor workerThreadExecutor;
    private final Executor uiThreadExecutor;

    private final MutableLiveData<Response<List<Repository>>> liveDataGetRepositoriesResponse;

    public ViewModelGitHubRepositoryList(GitHubRepository gitHubRepository, Executor workerThreadExecutor, Executor uiThreadExecutor) {
        this.gitHubRepository = gitHubRepository;
        this.workerThreadExecutor = workerThreadExecutor;
        this.uiThreadExecutor = uiThreadExecutor;
        liveDataGetRepositoriesResponse = new MutableLiveData<>();
    }

    public void getRepositories(final String userName) {
        liveDataGetRepositoriesResponse.setValue(new Response<List<Repository>>(null, true, false, 0));
        workerThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<Repository> repositories = gitHubRepository.getRepositoriesByUser(new User(userName));
                    if (repositories.isEmpty()) {
                        liveDataGetRepositoriesResponse.postValue(new Response<List<Repository>>(null, false, false, R.string.error_message_no_repositories));
                        return;
                    }
                    liveDataGetRepositoriesResponse.postValue(new Response<>(repositories, false, true, 0));
                } catch (GitHubRepositoryException e) {
                    e.printStackTrace();
                    switch (e.getErrorCode()) {
                        case GitHubRepositoryException.ERROR_IO:
                            liveDataGetRepositoriesResponse
                                    .postValue(new Response<List<Repository>>(null, false, false, R.string.error_message_io));
                            break;
                        case GitHubRepositoryException.ERROR_UNKNOWN_USER:
                            liveDataGetRepositoriesResponse
                                    .postValue(new Response<List<Repository>>(null, false, false, R.string.error_message_unknown_user));
                            break;
                        case GitHubRepositoryException.ERROR_OTHER:
                        default:
                            liveDataGetRepositoriesResponse
                                    .postValue(new Response<List<Repository>>(null, false, false, R.string.error_message_unknown_error));
                            break;

                    }
                }
            }
        });
    }

    public LiveData<Response<List<Repository>>> getLiveDataGetRepositoriesResponse() {
        return liveDataGetRepositoriesResponse;
    }
}
