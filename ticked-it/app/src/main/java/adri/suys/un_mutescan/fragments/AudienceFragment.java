package adri.suys.un_mutescan.fragments;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import adri.suys.un_mutescan.R;
import adri.suys.un_mutescan.model.Ticket;
import adri.suys.un_mutescan.presenter.AudiencePresenter;
import adri.suys.un_mutescan.utils.OnSwipeTouchListener;
import adri.suys.un_mutescan.viewinterfaces.AudienceRowViewInterface;
import adri.suys.un_mutescan.viewinterfaces.AudienceViewInterface;

public class AudienceFragment extends Fragment implements AudienceViewInterface {

    private ProgressBar progressBar;
    private Button inBtn, outBtn, allBtn;
    private View underlineIn, underlineOut, underlineAll;
    private RecyclerView recyclerView;
    private AudiencePresenter presenter;
    private AudienceAdapter adapter;
    private SearchView searchView;
    private static final int ALL = 1;
    private static final int IN = 2;
    private static final int OUT = 3;
    private int currentTab = ALL;
    private boolean isDarkTheme;

    public AudienceFragment(){
        // required
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_audience, container, false);
        presenter = new AudiencePresenter(this);
        progressBar = view.findViewById(R.id.audience_progressbar);
        showProgressBar();
        inBtn = view.findViewById(R.id.audience_btn_in);
        outBtn = view.findViewById(R.id.audience_btn_out);
        allBtn = view.findViewById(R.id.audience_btn_all);
        underlineAll = view.findViewById(R.id.audience_underline_all);
        underlineIn = view.findViewById(R.id.audience_underline_in);
        underlineOut = view.findViewById(R.id.audience_underline_out);
        setButtonsText();
        recyclerView = view.findViewById(R.id.audience_recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        allBtn.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.iceland_poppy));
        setActions();
        getAudience(ALL);
        searchView = view.findViewById(R.id.searchview_audience);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        handleSearch();
        clickAll();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        getAudience(ALL);
    }

    /**
     * Hides the ProgressBar
     */
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Update the adpater with the current state of the presenter
     * It updates the list of people we see on the screen
     */
    public void updateAudienceList() {
        if (adapter == null) {
            adapter = new AudienceAdapter(presenter);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setPresenter(presenter);
            adapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setActions(){
        inBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickIn();
            }
        });
        outBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOut();
            }
        });
        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAll();
            }
        });
        recyclerView.setOnTouchListener(new OnSwipeTouchListener(getContext()){
            public void onSwipeRight() {
                switch (currentTab){
                    case OUT : clickIn(); break;
                    case IN : clickAll(); break;
                    default: break;
                }
            }
            public void onSwipeLeft() {
                switch (currentTab){
                    case ALL : clickIn(); break;
                    case IN : clickOut(); break;
                    default: break;
                }
            }
        });
    }

    private void setButtonsText(){
        inBtn.setText(getResources().getString(R.string.audience_btn_in, presenter.getNbIn()));
        outBtn.setText(getResources().getString(R.string.audience_btn_out, presenter.getNbOut()));
        allBtn.setText(getResources().getString(R.string.audience_btn_all, presenter.getNbAll()));
    }

    private void getAudience(int options){
        presenter.getAudience(options);
    }

    private void clickAll(){
        if (isDarkTheme = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(PreferenceFragment.KEY_DARK_MODE, true)){
            inBtn.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.white));
            outBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.white));}
        else{
            inBtn.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.black));
            outBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }
        allBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.iceland_poppy));
        underlineIn.setBackgroundResource(0);
        underlineAll.setBackgroundResource(R.color.iceland_poppy);
        underlineOut.setBackgroundResource(0);
        showProgressBar();
        currentTab = ALL;
        searchView.setQuery("", false);
        getAudience(ALL);
    }

    private void clickIn(){
        inBtn.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.iceland_poppy));
        if (isDarkTheme = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(PreferenceFragment.KEY_DARK_MODE, true)){
            outBtn.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.white));
            allBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.white));}
        else{
            allBtn.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.black));
            outBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.black));}
        underlineIn.setBackgroundResource(R.color.iceland_poppy);
        underlineAll.setBackgroundResource(0);
        underlineOut.setBackgroundResource(0);
        showProgressBar();
        currentTab = IN;
        searchView.setQuery("", false);
        getAudience(IN);
    }

    private void clickOut(){
        if (isDarkTheme = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(PreferenceFragment.KEY_DARK_MODE, true)){
            inBtn.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.white));
            allBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.white));}
        else{
            inBtn.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.black));
            allBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.black));}
        outBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.iceland_poppy));
        underlineIn.setBackgroundResource(0);
        underlineAll.setBackgroundResource(0);
        underlineOut.setBackgroundResource(R.color.iceland_poppy);
        showProgressBar();
        currentTab = OUT;
        searchView.setQuery("", false);
        getAudience(OUT);
    }



    private void handleSearch(){
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(getActivity().getComponentName()));
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

    ////////////
    // HOLDER //
    ////////////

    public class AudienceHolder extends RecyclerView.ViewHolder implements AudienceRowViewInterface {

        TextView buyerName, seatNo, ticketType, cartNumber;
        Button copyBtn;

        AudienceHolder(View v){
            super(v);
            initViewElements(v);
        }

        public void displayInfos(String name, String ticketType, String seatNo, String cartNumber){
            this.buyerName.setText(name);
            this.seatNo.setText(seatNo);
            this.ticketType.setText(ticketType);
            this.cartNumber.setText(cartNumber);
        }

        private void initViewElements(View v){
            buyerName = v.findViewById(R.id.audience_buyer_name);
            seatNo = v.findViewById(R.id.audience_seat_no);
            ticketType = v.findViewById(R.id.audience_ticket_type);
            copyBtn = v.findViewById(R.id.copyBtn);
            cartNumber = v.findViewById(R.id.audience_cart_number);
            buyerName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return copyBarcode(view);
                }
            });
            seatNo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return copyBarcode(view);
                }
            });
            ticketType.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return copyBarcode(view);
                }
            });
            copyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    copyBarcode(view);
                }
            });
            cartNumber.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return copyBarcode(view);
                }
            });
        }

        public boolean copyBarcode(View view) {
            int currentPosition = this.getAdapterPosition();
            String barcodeValue = presenter.getBarcodeValue(currentPosition);
            ClipboardManager clipboardManager = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("barcode", barcodeValue);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(view.getContext(), "Valeur du QR-Code copié : "+barcodeValue, Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    /////////////
    // ADAPTER //
    /////////////

    private class AudienceAdapter extends RecyclerView.Adapter<AudienceHolder> implements Filterable {

        private AudiencePresenter presenter;

        AudienceAdapter(AudiencePresenter presenter){
            this.presenter = presenter;
        }

        @NonNull
        @Override
        public AudienceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_audience, viewGroup, false);
            return new AudienceHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull AudienceHolder audienceHolder, int i) {
            presenter.onViewCounterpartAtPosition(i, audienceHolder);
        }

        @Override
        public int getItemCount() {
            return presenter.getItemCount();
        }

        void setPresenter(AudiencePresenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String pattern = charSequence.toString().toLowerCase().trim();
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = presenter.getFilteredResult(pattern, currentTab);
                    return filterResults;
                }
                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    List<Ticket> filteredList = (List<Ticket>) filterResults.values;
                    presenter.notifyChanged(filteredList);
                }
            };
        }
    }

}
