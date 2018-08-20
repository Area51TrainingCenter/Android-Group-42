package pe.area51.notepad;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

public class FragmentList extends Fragment {


    public static final String TAG = "ListFragment";
    private static final int TEST_NOTES_SIZE = 100;

    private FragmentListInterface onNoteSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onNoteSelectedListener = ((FragmentListInterface) context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list, container, false);
        final ListView listViewElements = view.findViewById(R.id.listView);
        final ArrayAdapter<Note> noteArrayAdapter = new NoteAdapter(getActivity());
        listViewElements.setAdapter(noteArrayAdapter);
        listViewElements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Note note = noteArrayAdapter.getItem(i);
                if (onNoteSelectedListener != null) {
                    onNoteSelectedListener.onNoteSelected(note);
                }
            }
        });
        noteArrayAdapter.addAll(createNotes(TEST_NOTES_SIZE));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.note_list_title);
    }

    private ArrayList<Note> createNotes(final int quantity) {
        final ArrayList<Note> notes = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            notes.add(new Note(
                    "Title " + (i + 1),
                    getString(R.string.lorem_ipsum),
                    System.currentTimeMillis()
            ));
        }
        return notes;
    }

    public interface FragmentListInterface {

        void onNoteSelected(final Note note);

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
