package com.example.amitava.moviestalker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amitava.moviestalker.Utilities.GridItem;
import com.example.amitava.moviestalker.Utilities.NetworkUtilities;
import com.example.amitava.moviestalker.Utilities.PosterAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {

    GridView mMovieList;
    PosterAdapter mGridAdapter;
//    TextView mTesting;
    Spinner mSortBy;
    TextView mErrorMsg;
    ProgressBar mProgressBar;
    ArrayList<GridItem> mGridData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieList = (GridView) findViewById(R.id.gv_movie_list);
        //Initialize the GridView with no data
        mGridData = new ArrayList<>();
        mGridAdapter = new PosterAdapter(this, R.layout.grid_item_layout, mGridData);
        mMovieList.setAdapter(mGridAdapter);

//        mTesting = (TextView) findViewById(R.id.tv_test_url);

        //Initiate the Spinner and fill it with data and defining the tasks when item is selected
        mSortBy = (Spinner) findViewById(R.id.sp_sort_by);
        ArrayAdapter<CharSequence> sortByAdapter = ArrayAdapter.createFromResource(this,R.array.sort_by_option,R.layout.support_simple_spinner_dropdown_item);
        sortByAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSortBy.setAdapter(sortByAdapter);
        mSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if(item!=null){
//                    mTesting.setText(item.toString());

                    changeMovieSort(item.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mErrorMsg = (TextView) findViewById(R.id.tv_error_msg_disp);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mMovieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            Toast mPosterToast;
//
//            void showToast(String toastMessage){
//                if(mPosterToast != null){
//                    mPosterToast.cancel();
//                }
//                mPosterToast = makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT);
//                mPosterToast.show();
//            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                showToast("Pos: "+ position +" id:"+id);
                GridItem item = (GridItem) adapterView.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("image", item.getImage());
                intent.putExtra("release_date", item.getReleaseDate());
                intent.putExtra("rating", item.getRating());
                intent.putExtra("synopsis", item.getOverview());

                startActivity(intent);
            }
        });
    }

    private void changeMovieSort(String chosenOption){
        String temp;
        if(chosenOption.equals("Popularity")){
            temp = "popular";
        }else{
            temp = "top_rated";
        }
        URL movieDbSearchUrl = NetworkUtilities.buildUrl(temp);
        new MovieDbQueryTask().execute(movieDbSearchUrl);
//        mTesting.setText(movieDbSearchUrl.toString());
    }

    public void showJsonMessage() {
        mErrorMsg.setVisibility(View.INVISIBLE);
//        mTesting.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage() {
//        mTesting.setVisibility(View.INVISIBLE);
        mErrorMsg.setVisibility(View.VISIBLE);
    }

    private class MovieDbQueryTask extends AsyncTask<URL, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mTesting.setVisibility(View.INVISIBLE);
            mErrorMsg.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            //ensuring the page refreshes after option change
            mGridAdapter.notifyDataSetChanged();
            mGridAdapter.clear();
        }

        @Override
        protected String doInBackground(URL... urls) {
            Context context = MainActivity.this;
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
            URL queryUrl = urls[0];
            String movieDbQueryResult = null;
            if(isConnected){
                try{
                    movieDbQueryResult = NetworkUtilities.getResponseFromHttpUrl(queryUrl);
                } catch (IOException e){
                    e.printStackTrace();
                }
                try{
                    //Store the JSON Object
                    JSONObject jsonObject = new JSONObject(movieDbQueryResult);
                    //Retrieve the JSON Array
                    JSONArray movieResults = jsonObject.getJSONArray("results");
                    //GridItem Object to store the values for each item
                    GridItem item;
                    //Loop for retrieving all the Posters ID and storing in the view
                    for(int counter=0; counter<movieResults.length(); counter++){
                        JSONObject movie = movieResults.getJSONObject(counter);
                        String posterPath = movie.getString("poster_path");
                        String posterTitle = movie.getString("title");
                        String posterDetails = movie.getString("overview");
                        String posterReleaseDate = movie.getString("release_date");
                        String posterRating = movie.getString("vote_average");
                        item = new GridItem();
                        item.setTitle(posterTitle);
                        item.setImage("http://image.tmdb.org/t/p/w500/" +posterPath);
                        item.setOverview("Overview: "+posterDetails);
                        item.setRating("Rating: " +posterRating);

//                    DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
//                    String releaseDateToDisplay = null;
//                    try {
//                        Date date = format.parse(posterReleaseDate);
//                        releaseDateToDisplay = "Relase Date: "+ date;
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
                        item.setReleaseDate("Release Date: " +posterReleaseDate);
                        mGridData.add(item);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            return movieDbQueryResult;
        }

        @Override
        protected void onPostExecute(String s) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if(s!=null && !s.equals("")){
                showJsonMessage();
                mGridAdapter.setGridData(mGridData);
                mMovieList.setAdapter(mGridAdapter);
            }else {
                showErrorMessage();
            }
        }
    }
//private class SpinnerSortBy extends Activity implements AdapterView.OnItemSelectedListener{
//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//        String selectedOption = adapterView.getSelectedItem().toString();
//        changeMovieSort(selectedOption);
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }
//}
}
