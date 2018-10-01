package pe.area51.githublist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import pe.area51.githublist.ui.FragmentInteraction;

public class MainActivity extends AppCompatActivity implements FragmentInteraction {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setCustomView(R.layout.actionbar_edittext);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public EditText getActionBarEditText() {
        return getSupportActionBar().getCustomView().findViewById(R.id.editTextRepositoryName);
    }
}
