package adri.suys.un_mutescan.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import adri.suys.un_mutescan.R;
import adri.suys.un_mutescan.apirest.RestService;
import adri.suys.un_mutescan.model.Event;
import adri.suys.un_mutescan.presenter.EventPresenter;
import adri.suys.un_mutescan.viewinterfaces.EventListViewInterface;
import adri.suys.un_mutescan.viewinterfaces.EventRowViewInterface;

public class EventListActivity extends Activity implements EventListViewInterface{

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private ProgressBar progressBar;
    private EventPresenter presenter;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RestService restCommunication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        hideMenuItem(false);
        setElements();
        boolean forceRefresh = getIntent().getBooleanExtra("refresh", false);
        createEvents(forceRefresh);
        handleSearch();
        handlePullToRefresh();
        configActionBar(getString(R.string.my_events));
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    @Override
    void configActionBar(String title){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    /**
     * Hide the ProgressBar
     */
    public void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Update the adapter with the current state of the Presenter
     * @param isFilteredList a boolean indicating if we must pass the filtered list or
     *                       the normal list to the adapter
     */
    public void updateEventsList(boolean isFilteredList) {
        if (adapter == null) {
            adapter = new EventAdapter(presenter, isFilteredList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setPresenter(presenter);
            adapter.setFilteredList(isFilteredList);
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void showNoConnectionRetryToast() {
        showToast(getResources().getString(R.string.volley_error_no_connexion));
    }

    @Override
    public void showServerConnectionProblemToast() {
        showToast(getResources().getString(R.string.volley_error_server_error));
    }

    @Override
    public void collectEventsInDB(int id) {
        restCommunication.collectEvents(id);
    }

    public EventPresenter getPresenter() {
        return presenter;
    }

    public void createEvents(boolean forceRefresh) {
        presenter.collectEvents(forceRefresh, isInternetConnected());
    }

    /////////////////////
    // private methods //
    /////////////////////

    private void handleSearch(){
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.onActionViewExpanded();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
    }

    private void setElements(){
        progressBar = findViewById(R.id.progressBar_event);
        progressBar.setVisibility(View.VISIBLE);
        searchView = findViewById(R.id.searchview);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        presenter = new EventPresenter(this);
        restCommunication = new RestService(this);
        restCommunication.setEventPresenter(presenter);
        recyclerView = findViewById(R.id.event_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void handlePullToRefresh(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createEvents(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    ////////////
    // HOLDER //
    ////////////
    public class EventHolder extends RecyclerView.ViewHolder implements EventRowViewInterface {

        private Button eventBtn;
        private ImageView imageView;

        EventHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_event, parent, false));
            initViewElements();
        }

        /**
         * Displays the event name
         * @param name the name of the event
         */
        public void setEventName(String name){
            eventBtn.setText(name);
        }

        @Override
        public void setPastEventName(String eventName) {
            this.eventBtn.setText(getResources().getString(R.string.already_passed) + eventName);
        }

        private void initViewElements() {
            eventBtn = itemView.findViewById(R.id.event);
            //imageView = itemView.findViewById(R.id.event_img);
            setClickActions();
        }

        /**
         * When the user click on the STATS button, it changes the screen and the screen that
         * contains the stat of the event is displayed.
         */
        void setClickActions() {
            eventBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectEvent();
                }
            });
            /*imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectEvent();
                }
            });*/
        }

        private void selectEvent(){
            int currentPosition = this.getAdapterPosition();
            presenter.persistEvent(currentPosition);
            Intent intent = new Intent(EventListActivity.this, OneEventActivity.class);
            startActivity(intent);
        }

        public void setEventNameInRed() {
            eventBtn.setTextColor(Color.RED);
        }

        public void setEventNameInGreen(){
            eventBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.iceland_poppy));
        }

        @Override
        public void setImage(String photoPath) {
            //new DownLoadImageTask(imageView).execute(photoPath);
        }
    }

    /////////////
    // ADAPTER //
    /////////////
    @SuppressWarnings("unchecked")
    private class EventAdapter extends RecyclerView.Adapter<EventHolder> implements Filterable {

        private EventPresenter presenter;
        private boolean isFilteredList;

        EventAdapter(EventPresenter presenter, boolean isFilteredList) {
            this.presenter = presenter;
            this.isFilteredList = isFilteredList;
        }

        @NonNull
        @Override
        public EventHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(EventListActivity.this);
            return new EventHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull EventHolder eventHolder, int i) {
            presenter.onViewEventAtPosition(i, eventHolder, isFilteredList);
        }

        @Override
        public int getItemCount() {
            return presenter.getItemCount(isFilteredList);
        }

        void setPresenter(EventPresenter presenter) {
            this.presenter = presenter;
        }

        void setFilteredList(boolean filteredList) {
            isFilteredList = filteredList;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String pattern = charSequence.toString().toLowerCase().trim();
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = presenter.getFilteredResult(pattern);
                    return filterResults;
                }
                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    List<Event> filteredEvents = (List<Event>) filterResults.values;
                    presenter.notifyChanged(filteredEvents);
                }
            };
        }
    }

    /////////////////////////////////////////////////////
    // FETCH IMAGE FROM URL AND SAVE IT INTO IMAGEVIEW //
    /////////////////////////////////////////////////////
    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {

        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result){
            if (result != null) {
                imageView.setImageBitmap(result);
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(EventListActivity.this, R.drawable.artist_card_default));
            }
        }
    }

}
