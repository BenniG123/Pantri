package prodigy.pantri;

import android.os.Handler;
import android.os.Bundle;
import android.widget.ListView;

import prodigy.pantri.util.PantryItemAdapter;
import prodigy.pantri.util.ServerCommsTask;
import prodigy.pantri.util.TaskType;
public class ViewPantryActivity extends PantriBaseActivity implements Runnable {
    private Handler mHandler;
    private ServerCommsTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.content_view_pantry);
        setTitle("View Pantry");

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
                PantryItemAdapter pantryItemAdapter = new PantryItemAdapter(app, mTask.pantry);
                ListView listView = (ListView) findViewById(R.id.list_pantry);
                listView.setAdapter(pantryItemAdapter);
            }
        });
    }
}
