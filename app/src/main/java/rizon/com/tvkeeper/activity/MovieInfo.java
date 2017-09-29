package rizon.com.tvkeeper.activity;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import rizon.com.tvkeeper.R;
import rizon.com.tvkeeper.model.Movie;

public class MovieInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ImageView imageView = (ImageView) findViewById(R.id.Poster);
        TextView plot = (TextView) findViewById(R.id.Plot);
        plot.setMovementMethod(new ScrollingMovementMethod());
        Movie movie = (Movie) getIntent().getExtras().getSerializable("Movie");
        imageView.setImageBitmap(BitmapFactory.decodeFile(movie.getPosterPath()));
        plot.setText(movie.getPlot());
        getSupportActionBar().setTitle(movie.getTitle());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
