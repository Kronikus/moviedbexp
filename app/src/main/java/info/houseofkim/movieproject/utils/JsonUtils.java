package info.houseofkim.movieproject.utils;

import android.content.Context;
import android.util.Log;

import info.houseofkim.movieproject.MainActivity;
import info.houseofkim.movieproject.R;

import info.houseofkim.movieproject.model.MovieInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class JsonUtils extends MainActivity {

    private static List<String> getStringList(JSONArray jsonArray) {
        List<String> stringArray = new ArrayList<String>();
        if (jsonArray != null) {
            int length = jsonArray.length();
            //  stringArray =

            for (int i = 0; i < length; i++) {
                stringArray.add(jsonArray.optString(i));
            }
        }
        return stringArray;
    }

    public static MovieInfo parseMovieJSON(Context context,String json,int multi) {

        MovieInfo result = null;
        if (json != null) {
            try {
                JSONObject c = new JSONObject(json);
                int movieId = c.getInt(context.getString(R.string.movieId));
                String movieName = c.getString(context.getString(R.string.movieName));
                String movieReleaseDate = c.getString(  context.getString(R.string.movieReleaseDate));
                String movieImage = c.getString(  context.getString(R.string.image));
                String movieDescription = c.getString(context.getString(R.string.movieDescription));
                String movieDuration = "" ;//c.getString(  context.getString(R.string.movieDuration));
                double movieRating = 0.0;

                if (multi==0) {
                    try {
                        movieDuration = c.getString(context.getString(R.string.movieDuration));
                        movieRating = c.getDouble("vote_average");
                    } catch (final JSONException e1) {
                        Log.e(TAG, "Item not exist error: " + e1.getMessage());

                    }
                }

                result = new MovieInfo(movieId,movieName,movieReleaseDate,movieImage,movieDescription,movieDuration,movieRating);

            } catch (final JSONException e) {
                Log.e(TAG, "Item Json parsing error: " + e.getMessage());
            }

        } else {
            Log.e(TAG, "Item Couldn't get json.");

        }

        return result;
    }

    public static MovieInfo[] parseCatalogMovieJSON(Context context, String json) {

        MovieInfo[] result = null;
        MovieInfo movieItem;
       // Log.e("jsonArray",json.toString());
        if (json != null) {
            try {
                //Log.e("jsonArray",json.toString());
                JSONObject c = new JSONObject(json);
               // Log.e("jsonArray",c.toString());
                JSONArray b = c.getJSONArray(context.getString(R.string.results));
                //Log.e("jsonArray",b.toString());
                result = new MovieInfo[b.length()];
               // Log.e("Array",result.toString());
                for (int i=0;i < b.length();i++){
                  //  Log.e("index", String.valueOf(i) + " "+ String.valueOf(b.length()));

                   // Log.e("Array",String.valueOf(b.getJSONObject(i)));
                    movieItem = parseMovieJSON(context,String.valueOf(b.getJSONObject(i)),1);
                    result[i]=movieItem;
                   // Log.e("Array",String.valueOf(result[i]));

                }


//                List<String> alsoKnownAs = (List<String>) getStringList(b.getJSONArray(  context.getString(R.string.sandwich_alsoknownas)));
//                String placeOfOrigin = c.getString(  context.getString(R.string.sandwich_placeoforigin));
//                String description = c.getString(  context.getString(R.string.sandwich_description));
//                String image = c.getString(  context.getString(R.string.sandwich_image));
//                List<String> ingredients = getStringList(c.getJSONArray( context.getString(R.string.sandwich_ingredients)));
//                result = new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }

        } else {
            Log.e(TAG, "Couldn't get json.");

        }


        return result;
    }
}
