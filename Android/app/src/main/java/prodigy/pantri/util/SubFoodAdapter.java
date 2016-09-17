package prodigy.pantri.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Ben on 9/17/2016.
 */
public class SubFoodAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> names;

    public SubFoodAdapter(Context c, List<String> names) {
        mContext = c;
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
        SubFoodView ingredientView = null;

        if (convertView == null) {
            ingredientView = new SubFoodView(mContext, names.get(position));
        } else {
            ingredientView = (SubFoodView) convertView;
        }

        return ingredientView;
    }
}
