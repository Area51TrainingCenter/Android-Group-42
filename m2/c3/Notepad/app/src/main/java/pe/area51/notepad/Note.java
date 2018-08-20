package pe.area51.notepad;

import android.support.annotation.NonNull;

public class Note {

    @NonNull
    private final String title;

    @NonNull
    private final String content;
    private final long creationTimestamp;

    public Note(@NonNull String title, @NonNull String content, long creationTimestamp) {
        this.title = title;
        this.content = content;
        this.creationTimestamp = creationTimestamp;
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
}
