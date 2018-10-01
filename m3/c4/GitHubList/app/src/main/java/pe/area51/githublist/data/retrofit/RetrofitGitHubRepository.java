package pe.area51.githublist.data.retrofit;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pe.area51.githublist.domain.GitHubRepository;
import pe.area51.githublist.domain.GitHubRepositoryException;
import pe.area51.githublist.domain.Repository;
import pe.area51.githublist.domain.User;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitGitHubRepository implements GitHubRepository {

    private final GitHubService gitHubService;
    private final static String BASE_URL = "https://api.github.com/";

    public RetrofitGitHubRepository() {
        final Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        gitHubService = retrofit.create(GitHubService.class);
    }

    @NonNull
    @Override
    public List<Repository> getRepositoriesByUser(@NonNull User user) throws GitHubRepositoryException {
        try {
            final Call<List<pe.area51.githublist.data.retrofit.GitHubRepository>> call
                    = gitHubService.listRepositoriesByUserName(user.getUserName());
            final Response<List<pe.area51.githublist.data.retrofit.GitHubRepository>> response = call.execute();
            if (response.code() == 404) {
                throw new GitHubRepositoryException("Unknown user!", GitHubRepositoryException.ERROR_UNKNOWN_USER);
            }
            final List<pe.area51.githublist.data.retrofit.GitHubRepository> result
                    = response.body();
            final List<Repository> repositories = new ArrayList<>();
            for (final pe.area51.githublist.data.retrofit.GitHubRepository gitHubRepository : result) {
                repositories.add(new Repository(
                        String.valueOf(gitHubRepository.getId()),
                        gitHubRepository.getName(),
                        gitHubRepository.getUrl()
                ));
            }
            return repositories;
        } catch (IOException e) {
            e.printStackTrace();
            throw new GitHubRepositoryException(e.getMessage(), GitHubRepositoryException.ERROR_IO);
        } catch (GitHubRepositoryException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GitHubRepositoryException(e.getMessage(), GitHubRepositoryException.ERROR_OTHER);
        }
    }
}
