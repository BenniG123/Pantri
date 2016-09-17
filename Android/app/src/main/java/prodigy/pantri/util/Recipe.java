package prodigy.pantri.util;

import java.net.URL;
import java.util.List;

/**
 * Created by Ben on 9/17/2016.
 */
public class Recipe {
    public int id;
    public String name;
    public URL url;
    public URL image;
    public int calories;
    public List<Ingredient> ingredients;
    public List<String> steps;

    public Recipe () {

    }
}
