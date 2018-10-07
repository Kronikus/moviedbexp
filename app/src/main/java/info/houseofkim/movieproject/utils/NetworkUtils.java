package info.houseofkim.movieproject.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import info.houseofkim.movieproject.BuildConfig;
import info.houseofkim.movieproject.MainActivity;
import info.houseofkim.movieproject.R;

import static java.security.AccessController.getContext;


public class NetworkUtils extends MainActivity{

    private final static String key = BuildConfig.API_KEY;


    final static String MOVIEDB_BASE_URL =
            "https://api.themoviedb.org/3";

    final static String PARAM_CATALOG_POPULAR = "/movie/popular";
    final static String PARAM_CATALOG_TOPRATED = "/movie/top_rated";

    final static String PARAM_SEARCH = "search/movie";
    final static String PARAM_MOVIE = "movie";

    final static String PARAM_API_KEY = "api_key";
    final static String PARAM_LANG = "language";
    final static String PARAM_QUERY = "query";
    final static String PARAM_PAGE = "page";
    final static String PARAM_ADULT = "include_adult";

    final static String PARAM_VIDEO = "include_video";
    final static String PATH_VIDEO = "videos";
    final static String PATH_REVIEW = "reviews";

    final static String PARAM_SORT ="sort_by";

    public static URL buildUrl(String SearchQuery,int preference) {
        String PARAM_CATALOG = "/discover/movie";
        switch (preference) {

            case 0: PARAM_CATALOG = PARAM_CATALOG_POPULAR; break;
            case 1: PARAM_CATALOG = PARAM_CATALOG_TOPRATED;break;
        }
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL+ PARAM_CATALOG)
                .buildUpon()
                .appendQueryParameter(PARAM_API_KEY,key)
                .appendQueryParameter(PARAM_LANG,"en-US")
                .appendQueryParameter(PARAM_SORT,"popularity.desc")
                .appendQueryParameter(PARAM_ADULT,"false")
                .appendQueryParameter(PARAM_VIDEO,"false")
                .appendQueryParameter(PARAM_PAGE,"1")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
    public static URL buildUrlSingleMovie(String SearchMovieIdQuery) {

        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL)
                .buildUpon()
                .appendEncodedPath(PARAM_MOVIE)
                .appendPath(SearchMovieIdQuery)
                .appendQueryParameter(PARAM_API_KEY,key)
                .appendQueryParameter(PARAM_LANG,"en-US")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrlFetchMovieVideos(String SearchMovieIdQuery) {

        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL)
                .buildUpon()
                .appendEncodedPath(PARAM_MOVIE)
                .appendPath(SearchMovieIdQuery)
                .appendPath(PATH_VIDEO)
                .appendQueryParameter(PARAM_API_KEY,key)
                .appendQueryParameter(PARAM_LANG,"en-US")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrlFetchMovieReviews(String SearchMovieIdQuery) {

        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL)
                .buildUpon()
                .appendEncodedPath(PARAM_MOVIE)
                .appendPath(SearchMovieIdQuery)
                .appendPath(PATH_REVIEW)
                .appendQueryParameter(PARAM_API_KEY,key)
                .appendQueryParameter(PARAM_LANG,"en-US")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
       // Log.e("httpConnection",urlConnection.toString());
        try {
            InputStream in = urlConnection.getInputStream();
           // Log.e("httpResult",in.toString());
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
          //  Log.e("httpResult",scanner.next());
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                Log.d("httpResult", String.valueOf(hasInput));
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


    }
