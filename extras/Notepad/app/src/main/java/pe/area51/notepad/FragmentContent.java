package pe.area51.notepad;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class FragmentContent extends Fragment {

    private static final String KEY_ARG_NOTE_ID = "noteId";

    private TextView textViewTitle;
    private TextView textViewContent;

    private String noteId;

    private NotesRepository notesRepository;

    private boolean isDeletingNote;

    private FragmentInteractionListener fragmentInteractionListener;

    public static FragmentContent newInstance(final String noteId) {
        final FragmentContent contentFragment = new FragmentContent();
        final Bundle arguments = new Bundle();
        arguments.putString(KEY_ARG_NOTE_ID, noteId);
        contentFragment.setArguments(arguments);
        return contentFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInteractionListener = ((FragmentInteractionListener) context);
        notesRepository = ((Application) context.getApplicationContext()).getNotesRepository();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle arguments = getArguments();
        checkArguments(arguments);
        noteId = arguments.getString(KEY_ARG_NOTE_ID);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_content, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionDeleteNote) {
            deleteNoteAndFinish();
            return true;
        }
        return false;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isDeletingNote) {
            return;
        }
        updateNote();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_content, container, false);
        textViewTitle = view.findViewById(R.id.textViewNoteTitle);
        textViewContent = view.findViewById(R.id.textViewNoteContent);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showNote();
    }

    private static void checkArguments(final Bundle arguments) {
        if (arguments != null &&
                arguments.containsKey(KEY_ARG_NOTE_ID)) {
            return;
        }
        throw new RuntimeException("Fragment doesn't have needed arguments. Call newInstance() static creation method.");
    }

    private void deleteNoteAndFinish() {
        isDeletingNote = true;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                isDeletingNote = true;
            }

            @Override
            protected Void doInBackground(Void... voids) {
                notesRepository.deleteNoteById(noteId);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                fragmentInteractionListener.finishFragment();
            }
        }.execute();
    }

    private void updateNote() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                notesRepository.updateNote(new Note(
                        noteId,
                        textViewContent.getText().toString(),
                        textViewContent.getText().toString(),
                        System.currentTimeMillis()
                ));
                return null;
            }
        }.execute();
    }

    private void showNote() {
        new AsyncTask<Void, Void, Note>() {
            @Override
            protected Note doInBackground(Void... voids) {
                return notesRepository.getNoteById(noteId);
            }

            @Override
            protected void onPostExecute(Note note) {
                final Date noteDate = new Date(note.getCreationTimestamp());
                final DateFormat dateFormat = DateFormat.getInstance();
                getActivity().setTitle(dateFormat.format(noteDate));
                textViewTitle.setText(note.getTitle());
                textViewContent.setText(note.getContent());
            }
        }.execute();
    }
}
