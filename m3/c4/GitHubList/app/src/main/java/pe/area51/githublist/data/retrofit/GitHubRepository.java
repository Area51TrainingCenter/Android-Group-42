package pe.area51.githublist.data.retrofit;

import com.google.gson.annotations.SerializedName;

public class GitHubRepository {

    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("clone_url")
    private String url;

    public GitHubRepository(long id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
