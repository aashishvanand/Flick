package com.aashish.flick;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivityFragment extends Fragment {

    public View mainFragmentView;
    public String LOG_TAG = "ShowcaseFragment";
    public ArrayList<Movie> movies = new ArrayList<Movie>();
    private RequestQueue mRequestQueue;
    public ImageAdapter imageAdapter;
    public static MainActivityFragment instance;
    GridView gridview;
    public boolean isDualPane = false;
    public static String sortOrder = "popularity.desc", moreParams = "";
    public static boolean setting_cached = false;
    public int gridPos = -1;

    public MainActivityFragment() {
        instance = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainFragmentView = inflater.inflate(R.layout.fragment_main, container, false);
        mRequestQueue = Volley.newRequestQueue(getContext());
        imageAdapter = new ImageAdapter(getContext());
        gridview = (GridView) mainFragmentView.findViewById(R.id.gridView);
        gridview.setAdapter(imageAdapter);

        updateUI(setting_cached);
        gridview.setOnItemClickListener(new GridClickListener());
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            setGridColCount(3);
        else
            setGridColCount(2);

        return mainFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isDualPane = getPaneLayout();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("gridview_pos", gridview.getFirstVisiblePosition());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            gridPos = savedInstanceState.getInt("gridview_pos");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

    class GridClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (isDualPane){
                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                DetailActivityFragment detailActivityFragment = DetailActivityFragment.newInstance(movies.get(position));
                ft.replace(R.id.detailContainer, detailActivityFragment);
                ft.commit();
            } else {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, (Parcelable) movies.get(position));
                startActivity(intent);
            }
        }
    }

    public void updateUI(boolean cached){
        movies.clear();
        imageAdapter.clearItems();
        setting_cached = cached;
        if (!cached)
            getMovies(sortOrder, moreParams);
        else
            getFavorites();
    }

    public void getMovies(String sortOrder, String moreParams){
        String url = "http://api.themoviedb.org/3/discover/movie?sort_by=" + sortOrder + "&" + moreParams
            + "&api_key=" + BuildConfig.API;
        String url1 = "http://api.themoviedb.org/3/movie/now_playing?api_key=" + BuildConfig.API;
        if("now_playing".equals(sortOrder)) {
            JsonObjectRequest req = new JsonObjectRequest(url1, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray items = response.getJSONArray("results");
                                JSONObject movieObj;
                                for (int i = 0; i < items.length(); i++) {
                                    movieObj = items.getJSONObject(i);
                                    Movie movie = new Movie();
                                    movie.id = movieObj.getInt("id");
                                    movie.display_name = movieObj.getString("original_title");
                                    movie.overview = movieObj.getString("overview");
                                    movie.poster_url = "http://image.tmdb.org/t/p/w500/" + movieObj.getString("poster_path");
                                    movie.backdrop_url = "http://image.tmdb.org/t/p/w780/" + movieObj.getString("backdrop_path");
                                    movie.released_date = movieObj.getString("release_date");
                                    movie.rating = (float) movieObj.getDouble("vote_average");
                                    movie.popularity = movieObj.getDouble("popularity");
                                    movies.add(movie);
                                    imageAdapter.addItem(movie.poster_url);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    gridview.setAdapter(imageAdapter);
                                    if (gridPos > -1)
                                        gridview.setSelection(gridPos);
                                    gridPos = -1;
                                }
                            });
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            mRequestQueue.add(req);
        }
        else
        {
            JsonObjectRequest req = new JsonObjectRequest(url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray items = response.getJSONArray("results");
                                JSONObject movieObj;
                                for (int i = 0; i < items.length(); i++) {
                                    movieObj = items.getJSONObject(i);
                                    Movie movie = new Movie();
                                    movie.id = movieObj.getInt("id");
                                    movie.display_name = movieObj.getString("original_title");
                                    movie.overview = movieObj.getString("overview");
                                    movie.poster_url = "http://image.tmdb.org/t/p/w500/" + movieObj.getString("poster_path");
                                    movie.backdrop_url = "http://image.tmdb.org/t/p/w780/" + movieObj.getString("backdrop_path");
                                    movie.released_date = movieObj.getString("release_date");
                                    movie.rating = (float) movieObj.getDouble("vote_average");
                                    movie.popularity = movieObj.getDouble("popularity");
                                    movies.add(movie);
                                    imageAdapter.addItem(movie.poster_url);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    gridview.setAdapter(imageAdapter);
                                    if (gridPos > -1)
                                        gridview.setSelection(gridPos);
                                    gridPos = -1;
                                }
                            });
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            mRequestQueue.add(req);
        }

        }


    public void getFavorites(){
        movies.addAll((new MoviesDB()).getFavoriteMovies(getContext().getContentResolver()));
        for (Movie movie : movies){
            imageAdapter.addItem(movie.poster_url);
        }
        gridview.setAdapter(imageAdapter);
        if (gridPos > -1)
            gridview.setSelection(gridPos);
        gridPos = -1;
    }

    public void updateFavoritesGrid(){
        if (setting_cached) {
            int p = gridview.getLastVisiblePosition();
            updateUI(true);
            gridview.smoothScrollToPosition(p);
        }
    }

    public void setGridColCount(int n){
        ((GridView) mainFragmentView.findViewById(R.id.gridView)).setNumColumns(n);
    }

    public boolean getPaneLayout(){
        return (getActivity().findViewById(R.id.detailContainer) != null);
    }
}
