package prodigy.pantri.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import prodigy.pantri.R;
import prodigy.pantri.asynctasks.AddIngredientAsyncTask;
import prodigy.pantri.models.Ingredient;
import prodigy.pantri.util.PantriApplication;
import prodigy.pantri.util.PantriCallback;
import prodigy.pantri.asynctasks.ServerCommsTask;
import prodigy.pantri.models.TaskType;

public class NewFoodActivity extends AppCompatActivity implements PantriCallback<Ingredient> {

    int quantity = 1;
    private ServerCommsTask mTask;
    private Ingredient mIngred;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food);

        Intent i = getIntent();
        if (i.getStringExtra("name") != null) {
            TextView itemName = (TextView) findViewById(R.id.item_name);
            itemName.setText(i.getStringExtra("name"));
        }

        Button b = (Button) findViewById(R.id.btn_submit);
        final PantriCallback<Ingredient> that = this;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = ((TextView) findViewById(R.id.item_name)).getText().toString();

                mTask = new ServerCommsTask<>(TaskType.ADD_INGREDIENT, that, (PantriApplication) getApplication(), nameString, quantity);
                mTask.execute();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
    public void run(Ingredient arg) {
        mIngred = arg;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mIngred == null) {
                    Toast.makeText(getApplicationContext(), "Could not add that ingredient.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Successfully added " + mIngred.name + "!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "NewFood Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://prodigy.pantri/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "NewFood Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://prodigy.pantri/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
