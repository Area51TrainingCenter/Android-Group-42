package pe.area51.notepad.ui;

public interface FragmentInteractionListener {

    void showNote(final String noteId);

    void finishFragment();

    void setToolbarTitle(String title);
}
