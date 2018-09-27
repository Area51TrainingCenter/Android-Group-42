package pe.area51.notepad.ui;

import android.content.Context;
import android.support.v4.app.Fragment;

import pe.area51.notepad.Application;
import pe.area51.notepad.ViewModelFactory;

public class BaseFragment extends Fragment {

    protected FragmentInteractionListener fragmentInteractionListener;
    protected ViewModelFactory viewModelFactory;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInteractionListener = (FragmentInteractionListener) context;
        viewModelFactory = ((Application) context.getApplicationContext()).getViewModelFactory();
    }
}
