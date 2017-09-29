package rizon.com.tvkeeper.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rizon.com.tvkeeper.R;
import rizon.com.tvkeeper.adapter.CustomAdapter;
import rizon.com.tvkeeper.database.DBMovie;
import rizon.com.tvkeeper.model.Movie;
import rizon.com.tvkeeper.net.Utils;

public class MainActivity extends AppCompatActivity {
    private List<Movie> movies = new ArrayList<>();
    private ArrayAdapter<Movie> adapter;
    private DBMovie dbMovie;
    private Movie movie;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ListView list = (ListView) findViewById(R.id.list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkEnabled()) {
                    getInfoAboutMovieDFromIMDB();
                } else {
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast
                            .LENGTH_SHORT).show();
                }
            }
        });
        utils = new Utils();
        dbMovie = new DBMovie(this);
        movies = dbMovie.getAllMovieFromDB();
        adapter = new CustomAdapter<>(this, R.layout.item_element,
                movies);
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                dbMovie.deleteRecord(adapter.getItem(position).getId());
                adapter.remove(adapter.getItem(position));
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplication(), "Delete", Toast.LENGTH_LONG).show();

                return false;
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // showMovieDetails(position);
                Intent intent = new Intent(MainActivity.this, MovieInfo.class);
                intent.putExtra("Movie", adapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    private void showPopupMenu(View view, final int position) {
        PopupMenu menu = new PopupMenu(this, view);
        menu.inflate(R.menu.popup);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        dbMovie.deleteRecord(adapter.getItem(position).getId());
                        adapter.remove(adapter.getItem(position));
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplication(), "Delete", Toast.LENGTH_LONG).show();
                        return true;
                    default: return false;
                }

            }
        });
        menu.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isNetworkEnabled() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        return (netInfo != null) ? true : false;
    }

    @Nullable
    private Movie getInfoAboutMovieDFromIMDB() {
        LayoutInflater inflater = getLayoutInflater();
        View root = inflater.inflate(R.layout.search, null);
        final EditText getTitle = (EditText) root.findViewById(R.id.Title);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(root);
        builder.setTitle("Search");
        builder.setMessage("Enter Tv serial imdbID");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final ObjectMapper mapper = new ObjectMapper();
//                if (utils.isMovieAlreadyExist(getTitle.getText().toString(), movies)) {
                    String JSON = utils.getJsonFromRequest(getTitle.getText().toString());
                    if (JSON.contains("false")) {
                        Toast.makeText(getApplicationContext(), "Unknown TV serial",
                                Toast.LENGTH_LONG).show();
                   } else {
                        try {
                            movie = mapper.readValue(
                                    utils.getJsonFromRequest(getTitle.getText().toString()),
                                    Movie.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dbMovie.insert(movie);
                        movies.add(movie);
                    }
                    adapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(getApplicationContext(), "This movie already exist", Toast
//                            .LENGTH_LONG).show();
//                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
        return null;
    }


}
