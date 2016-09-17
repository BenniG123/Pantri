package prodigy.pantri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ViewPantryActivity extends PantriBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.content_view_pantry);
        setTitle("View Pantry");
    }
}
