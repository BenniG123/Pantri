package prodigy.pantri.util;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

/**
 * Created by Ben on 9/17/2016.
 */
public class Recipe implements Serializable {
    public int id;
    public String name;
    public String thumbnail;
    public String image;
    public String cookTime;
    public List<String> ingredients;
    public List<String> steps;
    public boolean isFavorite;

    public Recipe () {

    }
}
