package com.example.mymoviesapplication;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymoviesapplication.Inter.OnGetRepositoryCallback;
import com.example.mymoviesapplication.Inter.OnMoviesClickCallback;
import com.example.mymoviesapplication.adapter.MoviesAdapter;
import com.example.mymoviesapplication.database.FavoriteDatabase;
import com.example.mymoviesapplication.model.Genre;
import com.example.mymoviesapplication.model.Movie;
import com.example.mymoviesapplication.Inter.OnGetGenresCallback;
import com.example.mymoviesapplication.Inter.OnGetMoviesCallback;
import com.example.mymoviesapplication.repository.MoviesRepository;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recycleVewMoviesList;
    private MoviesAdapter adapter;
    private EditText searchTerm;


    private MoviesRepository moviesRepository;
    public static int i = 0;

    private List<Genre> movieGenres;
    public static FavoriteDatabase favoriteDatabase;
    private boolean isFetchingMovies;
    private int currentPage = 1;

    private String sortBy = MoviesRepository.POPULAR;
    private String sSearch = MoviesRepository.SEARCH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        moviesRepository = MoviesRepository.getInstance();
        recycleVewMoviesList = findViewById(R.id.movies_list);
        recycleVewMoviesList.setLayoutManager(new LinearLayoutManager(this));
        searchTerm = findViewById(R.id.searchTerm);
        favoriteDatabase = Room.databaseBuilder(getApplicationContext(), FavoriteDatabase.class, "myfavdb").allowMainThreadQueries().build();

        //search term
        searchTerm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                currentPage = 1;

                Log.i(TAG, "Search term entered is: " + searchTerm.getText().toString());
                searchMovieTitles(searchTerm.getText().toString(), currentPage);
                return false;
            }
        });
        setupOnScrollListener();
        getGenres();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * -depend de la item clicker se lance une methode :
     * - soit afficher les itemes de menu
     * - soit afficher la list favories.
     *
     * @param item
     * @return   - afficher les itemes de menu
     *           - afficher la list favorie
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                showSortMenu();
                return true;
            case R.id.favoris:
                startActivity(new Intent(MainActivity.this, FavoriteListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * afficher les item du menu
     */
    private void showSortMenu() {
        PopupMenu sortMenu = new PopupMenu(this, findViewById(R.id.sort));
        sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                /*
                 * Every time we sort, we need to go back to page 1
                 */
                currentPage = 1;

                switch (item.getItemId()) {
                    case R.id.popular:
                        sortBy = MoviesRepository.POPULAR;
                        getMovies(currentPage);
                        return true;
                    case R.id.top_rated:
                        sortBy = MoviesRepository.TOP_RATED;
                        getMovies(currentPage);
                        return true;
                    case R.id.now_playing:
                        sortBy = MoviesRepository.NOW_PLAYING;
                        getMovies(currentPage);
                        return true;//fin
                    case R.id.upcoming:
                        sortBy = MoviesRepository.UPCOMING;
                        getMovies(currentPage);
                        return true;
                    default:
                        return false;
                }
            }
        });
        sortMenu.inflate(R.menu.menu_movies_sort);
        sortMenu.show();
    }

    /**
     *
     */
    private void setupOnScrollListener() {
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        recycleVewMoviesList.setLayoutManager(manager);
        recycleVewMoviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        getMovies(currentPage + 1);
                    }
                }
            }
        });
    }

    private void getGenres() {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                movieGenres = genres;
                getMovies(currentPage);
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    /**
     * recuperer the movie par rapport a une page
     *
     * @param page
     */

    private void getMovies(int page) {
        isFetchingMovies = true;
        moviesRepository.getMovies(page, sortBy, new OnGetMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                Log.d("MoviesRepository", "Current Page = " + page);
                if (adapter == null) {
                    adapter = new MoviesAdapter(movies, movieGenres, callback);
                    recycleVewMoviesList.setAdapter(adapter);
                } else {
                    if (page == 1) {
                        adapter.clearMovies();
                    }
                    adapter.appendMovies(movies);
                }
                currentPage = page;
                isFetchingMovies = false;
                setTitle();
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    /**
     * Receives the search query from the View and calls the repository
     *
     * @param title
     */
    private void searchMovieTitles(String title, int page) {
        Log.i(TAG, "Search term entered is: " + title);
        isFetchingMovies = true;
        moviesRepository.searchMovies(title, sSearch, page,
                new OnGetRepositoryCallback() {
                    @Override
                    public void onSuccess(int page, List<Movie> movies) {
                        if (adapter == null) {
                            adapter = new MoviesAdapter(movies, movieGenres, callback);
                            recycleVewMoviesList.setAdapter(adapter);
                        } else {
                            if (currentPage == 1) {
                                adapter.clearMovies();
                            }
                            adapter.appendMovies(movies);
                        }
                        currentPage = page;
                        isFetchingMovies = false;
                        setTitle(getString(R.string.enter_search_keyword));

                    }

                    @Override
                    public void onError() {
                        showError();
                    }
                });

    }

    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(MainActivity.this, MovieActivity.class);
            intent.putExtra(MovieActivity.MOVIE_ID, movie.getId());
            startActivity(intent);
        }
    };


    private void setTitle() {
        switch (sortBy) {
            case MoviesRepository.POPULAR:
                setTitle(getString(R.string.popular));
                break;
            case MoviesRepository.TOP_RATED:
                setTitle(getString(R.string.top_rated));
                break;
            case MoviesRepository.UPCOMING:
                setTitle(getString(R.string.upcoming));
                break;
            case MoviesRepository.NOW_PLAYING:
                setTitle(getString(R.string.now_playing));
                break;
        }
    }

    private void showError() {
        Toast.makeText(MainActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        /*Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}