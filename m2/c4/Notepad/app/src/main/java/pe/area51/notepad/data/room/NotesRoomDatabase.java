package pe.area51.notepad.data.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NotesRoomDatabase extends RoomDatabase {

    public abstract NotesDao getNotesDao();
}
