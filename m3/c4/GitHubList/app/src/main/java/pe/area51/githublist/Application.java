package pe.area51.githublist;

import java.util.concurrent.Executor;

import pe.area51.githublist.data.retrofit.RetrofitGitHubRepository;
import pe.area51.githublist.domain.GitHubRepository;
import pe.area51.githublist.executors.DefaultThreadPoolExecutor;
import pe.area51.githublist.executors.UiThreadExecutor;
import pe.area51.githublist.ui.repository_list.ViewModelFactory;

public class Application extends android.app.Application {

    private GitHubRepository gitHubRepository;

    private ViewModelFactory viewModelFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        Executor workerThreadExecutor = new DefaultThreadPoolExecutor();
        Executor uiThreadExecutor = new UiThreadExecutor();
        gitHubRepository = new RetrofitGitHubRepository();
        viewModelFactory = new ViewModelFactory(
                gitHubRepository,
                workerThreadExecutor,
                uiThreadExecutor
        );
    }

    public ViewModelFactory getViewModelFactory() {
        return viewModelFactory;
    }

    public GitHubRepository getGitHubRepository() {
        return gitHubRepository;
    }
}
