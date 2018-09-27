package pe.area51.notepad.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import pe.area51.notepad.Note;
import pe.area51.notepad.R;
import pe.area51.notepad.ui.BaseFragment;

public class FragmentList extends BaseFragment {

    public static final String TAG = "ListFragment";

    private ArrayAdapter<Note> notesArrayAdapter;

    private ViewModelList viewModelList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModelList = ViewModelProviders
                .of(this, viewModelFactory).get(ViewModelList.class);
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
        viewModelList.fetchNotes();
        viewModelList.getLiveDataNotes().observe(this, new Observer<LiveData<List<Note>>>() {
            @Override
            public void onChanged(@Nullable LiveData<List<Note>> listLiveData) {
                listLiveData.observe(FragmentList.this, new Observer<List<Note>>() {
                    @Override
                    public void onChanged(@Nullable List<Note> notes) {
                        notesArrayAdapter.clear();
                        notesArrayAdapter.addAll(notes);
                    }
                });
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.note_list_title);
    }

    private void createNote() {
        viewModelList.createEmptyNote();
        viewModelList.getLiveDataCreatedNote().observe(this, new Observer<Note>() {
            @Override
            public void onChanged(@Nullable Note note) {
                if (note == null) {
                    return;
                }
                fragmentInteractionListener.showNote(note.getId());
                viewModelList.getLiveDataCreatedNote().setValue(null);
            }
        });
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
            String noteTitle = note.getTitle().trim();
            if (noteTitle.length() == 0) {
                noteTitle = getContext().getString(R.string.untitled_note);
            }
            viewHolder.titleTextView.setText(noteTitle);
            viewHolder.thumbnailImageView.setImageDrawable(
                    TextDrawable
                            .builder()
                            .buildRound(
                                    String.valueOf(noteTitle.charAt(0)),
                                    colorGenerator.getColor(position)
                            )
            );
            return view;
        }
    }
}
