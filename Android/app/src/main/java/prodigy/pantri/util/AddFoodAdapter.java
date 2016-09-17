package prodigy.pantri.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Ben on 9/17/2016.
 */
public class AddFoodAdapter extends BaseAdapter {

    private Context mContext;
    private List<Integer> images;
    private List<String> names;

    public AddFoodAdapter(Context c, List<Integer> images, List<String> names) {
        mContext = c;
        this.images = images;
        this.names = names;
    }

    public int getCount() {
        return names.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        AddFoodView addFoodView = null;

        if (convertView == null) {
            addFoodView = new AddFoodView(mContext, images.get(position), names.get(position));
        } else {
            addFoodView = (AddFoodView) convertView;
        }

        return addFoodView;
    }
}
