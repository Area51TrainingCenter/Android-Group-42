package pe.area51.notepad.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import pe.area51.notepad.R;
import pe.area51.notepad.ui.FragmentInteractionListener;
import pe.area51.notepad.ui.content.FragmentContent;
import pe.area51.notepad.ui.list.FragmentList;

public class MainActivity extends AppCompatActivity implements FragmentInteractionListener {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(pe.area51.notepad.R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            /*
            Creamos los fragments e iniciamos la transacción cuando este Activity se cree
            por primera vez.
             */
            final FragmentList fragmentList = new FragmentList();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragmentList)
                    .commit();
        }
        /*
        Si el Activity tiene un estado anterior, no es necesario crear los fragments o ejecutar nuevamente
        la transacción, puesto que en el proceso de recreación de estados, se recrea también el estado
        del FragmentManager.
        */
    }

    @Override
    public void showNote(String noteId) {
        final FragmentContent fragmentContent = FragmentContent.newInstance(noteId);
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragmentContainer, fragmentContent)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void finishFragment() {
        onBackPressed();
    }

    @Override
    public void setToolbarTitle(String title) {
        setTitle(title);
    }
}
