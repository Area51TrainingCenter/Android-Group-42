package pe.area51.notepad;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

public class FragmentList extends Fragment {


    public static final String TAG = "ListFragment";

    private FragmentInteractionListener fragmentInteractionListener;

    private NotesRepository notesRepository;

    private ArrayAdapter<Note> notesArrayAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInteractionListener = ((FragmentInteractionListener) context);
        notesRepository = ((Application) context.getApplicationContext()).getNotesRepository();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionCreateNote:
                createNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list, container, false);
        final ListView listViewElements = view.findViewById(R.id.listView);
        notesArrayAdapter = new NoteAdapter(getActivity());
        listViewElements.setAdapter(notesArrayAdapter);
        listViewElements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Note note = notesArrayAdapter.getItem(i);
                fragmentInteractionListener.showNote(note.getId());
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.note_list_title);
    }

    private void createNote() {
        new AsyncTask<Void, Void, Note>() {
            @Override
            protected Note doInBackground(Void... voids) {
                final Note note = new Note(
                        "",
                        "",
                        System.currentTimeMillis()
                );
                return notesRepository.insertNote(note);
            }

            @Override
            protected void onPostExecute(Note note) {
                fragmentInteractionListener.showNote(note.getId());
            }
        }.execute();
    }

    private void refreshList() {
        new AsyncTask<Void, Void, List<Note>>() {
            @Override
            protected List<Note> doInBackground(Void... voids) {
                return notesRepository.getNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                notesArrayAdapter.clear();
                notesArrayAdapter.addAll(notes);
            }
        }.execute();
    }

    private static class NoteAdapter extends ArrayAdapter<Note> {

        private final LayoutInflater layoutInflater;
        private final ColorGenerator colorGenerator;

        public NoteAdapter(final Context context) {
            super(context, 0);
            layoutInflater = LayoutInflater.from(getContext());
            colorGenerator = ColorGenerator.MATERIAL;
        }

        private static class ViewHolder {
            TextView titleTextView;
            ImageView thumbnailImageView;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d(TAG, "Position: " + position + "; convertView " + (convertView == null ? "== null" : "!= null"));
            final Note note = getItem(position);
            final View view;
            final ViewHolder viewHolder;
            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.element_note, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.titleTextView = view.findViewById(R.id.textViewNoteTitle);
                viewHolder.thumbnailImageView = view.findViewById(R.id.imageViewThumbnail);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.titleTextView.setText(note.getTitle());
            viewHolder.thumbnailImageView.setImageDrawable(
                    TextDrawable
                            .builder()
                            .buildRound(
                                    "" + note.getTitle().charAt(0),
                                    colorGenerator.getColor(position)
                            )
            );
            return view;
        }
    }
}
