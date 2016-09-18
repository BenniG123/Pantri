package prodigy.pantri;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import prodigy.pantri.util.PantriApplication;
import prodigy.pantri.util.ServerCommsTask;
import prodigy.pantri.util.TaskType;

public class NewFoodActivity extends AppCompatActivity implements Runnable {

    int quantity = 1;
    private ServerCommsTask mTask;
    private Handler mHandler;
    private NewFoodActivity mNewFoodActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food);

        Intent i = getIntent();
        if (i.getStringExtra("name") != null) {
            TextView itemName = (TextView) findViewById(R.id.item_name);
            itemName.setText(i.getStringExtra("name"));
        }

        mNewFoodActivity = this;

        Button b = (Button) findViewById(R.id.btn_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = ((TextView) findViewById(R.id.item_name)).getText().toString();

                mTask = new ServerCommsTask(TaskType.ADD_INGREDIENT, (PantriApplication) getApplication(), nameString, quantity);
                mTask.execute();

                mHandler = new Handler();
                mHandler.post(mNewFoodActivity);
            }
        });
    }

    public void subQuantity(View v) {
        if (quantity > 0) {
            quantity--;
        }

        ((TextView) findViewById(R.id.quantity)).setText(String.valueOf(quantity));
    }

    public void addQuantity(View v) {
        quantity++;
        ((TextView) findViewById(R.id.quantity)).setText(String.valueOf(quantity));
    }

    @Override
    public void run() {
        while(!mTask.opDone) ;
        finish();
    }
}
