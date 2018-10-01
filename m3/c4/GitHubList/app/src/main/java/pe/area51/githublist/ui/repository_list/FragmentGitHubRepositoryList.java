package pe.area51.githublist.ui.repository_list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import pe.area51.githublist.Application;
import pe.area51.githublist.R;
import pe.area51.githublist.domain.Repository;
import pe.area51.githublist.ui.FragmentInteraction;
import pe.area51.githublist.ui.Response;

public class FragmentGitHubRepositoryList extends Fragment {

    private TextView textViewErrorMessage;
    private ProgressBar progressBar;
    private ListView listViewRepositories;

    private FragmentInteraction fragmentInteraction;
    private ViewModelGitHubRepositoryList viewModelGitHubRepositoryList;

    private RepositoriesAdapter repositoriesAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Application application = ((Application) context.getApplicationContext());
        final ViewModelFactory viewModelFactory = application.getViewModelFactory();
        viewModelGitHubRepositoryList = ViewModelProviders.of(this, viewModelFactory).get(ViewModelGitHubRepositoryList.class);
        fragmentInteraction = ((FragmentInteraction) context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_github_repository_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionSearchRepositories) {
            onSearch(fragmentInteraction.getActionBarEditText().getText().toString());
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_github_repository_list, container, false);
        textViewErrorMessage = view.findViewById(R.id.textViewErrorMessage);
        progressBar = view.findViewById(R.id.progressBar);
        listViewRepositories = view.findViewById(R.id.listViewRepositories);
        repositoriesAdapter = new RepositoriesAdapter(getContext());
        listViewRepositories.setAdapter(repositoriesAdapter);
        listViewRepositories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Repository repository = repositoriesAdapter.getItem(position);
                final Intent uriIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(repository.getUrl()));
                startActivity(uriIntent);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentInteraction.getActionBarEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onSearch(fragmentInteraction.getActionBarEditText().getText().toString());
                    return true;
                }
                return false;
            }
        });
        viewModelGitHubRepositoryList.getLiveDataGetRepositoriesResponse().observe(getViewLifecycleOwner(), new Observer<Response<List<Repository>>>() {
            @Override
            public void onChanged(@Nullable Response<List<Repository>> listResponse) {
                if (listResponse == null) {
                    return;
                }
                if (listResponse.isLoading()) {
                    showProgressBar();
                    return;
                }
                if (!listResponse.wasSuccessful()) {
                    showErrorMessage(listResponse.getErrorMessage());
                    return;
                }
                showListViewRepositories();
                repositoriesAdapter.clear();
                repositoriesAdapter.addAll(listResponse.getSuccessData());
            }
        });
    }

    private void onSearch(final String searchText) {
        viewModelGitHubRepositoryList.getRepositories(searchText);
    }

    private void showListViewRepositories() {
        textViewErrorMessage.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        listViewRepositories.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        textViewErrorMessage.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        listViewRepositories.setVisibility(View.GONE);
    }

    private void showErrorMessage(@StringRes final int errorMessage) {
        textViewErrorMessage.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        listViewRepositories.setVisibility(View.GONE);
        textViewErrorMessage.setText(errorMessage);
    }

    private static class RepositoriesAdapter extends ArrayAdapter<Repository> {

        private final LayoutInflater layoutInflater;
        private final ColorGenerator colorGenerator;

        public RepositoriesAdapter(final Context context) {
            super(context, 0);
            layoutInflater = LayoutInflater.from(getContext());
            colorGenerator = ColorGenerator.MATERIAL;
        }

        private static class ViewHolder {
            TextView nameTextView;
            ImageView thumbnailImageView;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Repository repository = getItem(position);
            final View view;
            final ViewHolder viewHolder;
            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.element_github_repository, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.nameTextView = view.findViewById(R.id.textViewRepositoryTitle);
                viewHolder.thumbnailImageView = view.findViewById(R.id.imageViewThumbnail);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final String name = repository.getName().trim();
            viewHolder.nameTextView.setText(name);
            viewHolder.thumbnailImageView.setImageDrawable(
                    TextDrawable
                            .builder()
                            .buildRound(
                                    name.length() > 0 ? String.valueOf(name.charAt(0)) : "",
                                    colorGenerator.getColor(position)
                            )
            );
            return view;
        }
    }
}
