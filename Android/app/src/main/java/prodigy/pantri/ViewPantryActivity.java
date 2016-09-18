package prodigy.pantri;

import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import prodigy.pantri.util.Ingredient;
import prodigy.pantri.util.PantryItemAdapter;
import prodigy.pantri.util.ServerCommsTask;
import prodigy.pantri.util.TaskType;

public class ViewPantryActivity extends PantriBaseActivity implements Runnable {
    private Handler mHandler;
    private ServerCommsTask mTask;
    private List<Ingredient> mIngredientList;
    private ViewPantryActivity mViewPantryActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.content_view_pantry);
        setTitle("View Pantry");
        mIngredientList = new ArrayList<>();
        mViewPantryActivity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        // Get list of ingredients
        mTask = new ServerCommsTask(TaskType.GET_PANTRY, app);
        mTask.execute();

        mHandler = new Handler();
        mHandler.post(this);
    }

    @Override
    public void run() {
        while (mTask.pantry == null);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIngredientList = mTask.pantry;
                PantryItemAdapter pantryItemAdapter = new PantryItemAdapter(app, mTask.pantry, mViewPantryActivity);
                ListView listView = (ListView) findViewById(R.id.list_pantry);
                listView.setAdapter(pantryItemAdapter);
            }
        });
    }
}
