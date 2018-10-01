package pe.area51.githublist.domain;

import android.support.annotation.NonNull;

import java.util.List;

public interface GitHubRepository {

    @NonNull
    List<Repository> getRepositoriesByUser(@NonNull User user) throws GitHubRepositoryException;

}
