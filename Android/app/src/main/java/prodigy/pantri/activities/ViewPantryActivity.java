package prodigy.pantri.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import prodigy.pantri.R;
import prodigy.pantri.adapters.PantryItemAdapter;
import prodigy.pantri.asynctasks.ViewPantryAsyncTask;
import prodigy.pantri.models.Ingredient;
import prodigy.pantri.util.PantriApplication;
import prodigy.pantri.util.PantriCallback;
import prodigy.pantri.asynctasks.ServerCommsTask;
import prodigy.pantri.models.TaskType;

public class ViewPantryActivity extends PantriBaseActivity implements PantriCallback<List<Ingredient>> {

    private ViewPantryAsyncTask mTask;
    private List<Ingredient> mIngredientList;
    private ViewPantryActivity mViewPantryActivity;
    private View mListView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.activity_view_pantry);
        setTitle("View Pantry");
        mIngredientList = new ArrayList<>();
        mViewPantryActivity = this;
        mListView = findViewById(R.id.list_pantry);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        // Get list of ingredients
        mTask = new ViewPantryAsyncTask((PantriApplication) getApplication(), this);
        mTask.execute();
        showProgress(true);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mListView.setVisibility(show ? View.GONE : View.VISIBLE);
            mListView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mListView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mListView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void run(List<Ingredient> pantry) {
        mIngredientList = pantry;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                PantryItemAdapter pantryItemAdapter = new PantryItemAdapter(app, mIngredientList, mViewPantryActivity);
                Collections.sort(mIngredientList, new Comparator<Ingredient>() {
                    @Override
                    public int compare(Ingredient o1, Ingredient o2) {
                        return o1.name.compareToIgnoreCase(o2.name);
                    }
                });

                showProgress(false);
                ListView listView = (ListView) findViewById(R.id.list_pantry);
                listView.setAdapter(pantryItemAdapter);
            }
        });
    }
}
