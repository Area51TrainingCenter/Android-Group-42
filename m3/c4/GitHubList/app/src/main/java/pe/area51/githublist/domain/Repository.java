package pe.area51.githublist.domain;

import android.support.annotation.NonNull;

public class Repository {
    @NonNull
    private final String id;
    @NonNull
    private final String name;
    @NonNull
    private final String url;

    public Repository(@NonNull String id, @NonNull String name, @NonNull String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repository that = (Repository) o;
        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;
        return url.equals(that.url);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Repository{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
