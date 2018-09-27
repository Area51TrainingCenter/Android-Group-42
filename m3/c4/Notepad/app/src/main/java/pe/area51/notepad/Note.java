package pe.area51.notepad;

import android.support.annotation.NonNull;

public class Note {

    @NonNull
    private final String id;
    @NonNull
    private final String title;

    @NonNull
    private final String content;
    private final long creationTimestamp;

    public Note(@NonNull String title, @NonNull String content, long creationTimestamp) {
        this.id = "";
        this.title = title;
        this.content = content;
        this.creationTimestamp = creationTimestamp;
    }

    public Note(@NonNull String id, @NonNull String title, @NonNull String content, long creationTimestamp) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creationTimestamp = creationTimestamp;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (creationTimestamp != note.creationTimestamp) return false;
        if (!id.equals(note.id)) return false;
        if (!title.equals(note.title)) return false;
        return content.equals(note.content);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + (int) (creationTimestamp ^ (creationTimestamp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", creationTimestamp=" + creationTimestamp +
                '}';
    }
}
