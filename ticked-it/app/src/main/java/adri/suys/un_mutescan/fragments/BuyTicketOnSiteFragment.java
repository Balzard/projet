package adri.suys.un_mutescan.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Objects;

import adri.suys.un_mutescan.R;
import adri.suys.un_mutescan.activities.Activity;
import adri.suys.un_mutescan.activities.PayActivity;
import adri.suys.un_mutescan.presenter.BuyTicketOnSitePresenter;
import adri.suys.un_mutescan.viewinterfaces.BuyTicketOnSiteViewInterface;
import adri.suys.un_mutescan.viewinterfaces.PurchaseRowViewInterface;

public class BuyTicketOnSiteFragment extends Fragment implements BuyTicketOnSiteViewInterface {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TicketTypeAdapter adapter;
    private BuyTicketOnSitePresenter presenter;
    private int cpt = 0;

    public BuyTicketOnSiteFragment(){
        // required
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_ticket_onsite, container, false);
        progressBar = view.findViewById(R.id.progressBar_ticket_type);
        progressBar.setVisibility(View.VISIBLE);
        presenter = new BuyTicketOnSitePresenter(this);
        recyclerView = view.findViewById(R.id.ticket_type_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        getTicketTypes();
        AppCompatButton validateBtn = view.findViewById(R.id.add_ticket_validate_btn);
        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateSell();
            }
        });
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        getTicketTypes();
    }

    private void validateSell(){
        if (cpt > 0){
            if (presenter.isEventSoldout(cpt)){
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AppCompatAlertDialogStyle);
                dialogBuilder.setMessage(Objects.requireNonNull(getActivity()).getResources().getString(R.string.soldout)).setTitle("");
                dialogBuilder.setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                initiateSell();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alert = dialogBuilder.create();
                alert.setTitle("");
                alert.show();
            } else {
                initiateSell();
            }
        } else {
            ((Activity) Objects.requireNonNull(getActivity())).showToast(Objects.requireNonNull(getActivity()).getResources().getString(R.string.min_one_cp_selected));
        }
    }

    public void initiateSell(){
        getCart();
        String message = getResources().getString(R.string.order_recap, presenter.getTotalTicketSold(), presenter.getCartAmount());
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AppCompatAlertDialogStyle);
        dialogBuilder.setMessage(message).setTitle("");
        dialogBuilder.setCancelable(false)
                .setPositiveButton(R.string.validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBar.setVisibility(View.VISIBLE);
                        presenter.completeCart();
                        dialogInterface.cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = dialogBuilder.create();
        alert.setTitle("");
        alert.show();
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public void updateTicketTypesList() {
        if (adapter == null) {
            adapter = new BuyTicketOnSiteFragment.TicketTypeAdapter(presenter);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setPresenter(presenter);
            adapter.notifyDataSetChanged();
        }
    }

    public void changeScreen(double cartAmount, int nbTickets) {
        Intent i = new Intent(getActivity(), PayActivity.class);
        i.putExtra("price", cartAmount);
        i.putExtra("nbTicketsSold", nbTickets);
        startActivity(i);
    }

    private void getTicketTypes(){
        presenter.collectCounterparts();
    }

    private void getCart(){
        for (int i=0; i<recyclerView.getChildCount(); i++){
            TicketTypeHolder th = (TicketTypeHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            String name = th.ticketName.getText().toString();
            double price = Double.parseDouble(th.ticketPrice.getText().toString());
            int quantity = Integer.parseInt(th.numberOfTicketWanted.getText().toString());
            presenter.setQuantityToCp(name, price, quantity);
        }
    }

    ////////////
    // HOLDER //
    ////////////

    public class TicketTypeHolder extends RecyclerView.ViewHolder implements PurchaseRowViewInterface {

        private TextView ticketName, ticketPrice, numberOfTicketWanted;
        private Button minusBtn, plusBtn;

        TicketTypeHolder(View v){
            super(v);
            initViewElements(v);
        }

        public void displayInfos(String name, String price){
            ticketName.setText(name);
            ticketPrice.setText(price);
        }

        private void initViewElements(View v){
            ticketName = v.findViewById(R.id.item_ticket_name);
            ticketPrice = v.findViewById(R.id.item_ticket_price);
            numberOfTicketWanted = v.findViewById(R.id.item_ticket_nb_purchases);
            minusBtn = v.findViewById(R.id.item_ticket_minus_btn);
            plusBtn = v.findViewById(R.id.item_ticket_plus_btn);
            setClickActions();
        }

        private void setClickActions(){
            minusBtn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View view) {
                    int currentNbOfPurchase = Integer.parseInt(numberOfTicketWanted.getText().toString());
                    if (currentNbOfPurchase > 0){
                        currentNbOfPurchase--;
                        numberOfTicketWanted.setText(Integer.toString(currentNbOfPurchase));
                        cpt --;
                    }
                }
            });
            plusBtn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View view) {
                    int currentNbOfPurchase = Integer.parseInt(numberOfTicketWanted.getText().toString());
                    currentNbOfPurchase++;
                    numberOfTicketWanted.setText(Integer.toString(currentNbOfPurchase));
                    cpt++;
                }
            });
        }
    }

    /////////////
    // ADAPTER //
    /////////////

    private class TicketTypeAdapter extends RecyclerView.Adapter<TicketTypeHolder> {

        private BuyTicketOnSitePresenter presenter;

        TicketTypeAdapter(BuyTicketOnSitePresenter presenter){
            this.presenter = presenter;
        }

        @NonNull
        @Override
        public TicketTypeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_counterpart, viewGroup, false);
            return new TicketTypeHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull TicketTypeHolder ticketTypeHolder, int i) {
            presenter.onViewCounterpartAtPosition(i, ticketTypeHolder);
        }

        @Override
        public int getItemCount() {
            return presenter.getItemCount();
        }

        void setPresenter(BuyTicketOnSitePresenter presenter) {
            this.presenter = presenter;
        }
    }

}
