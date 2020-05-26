package adri.suys.un_mutescan.fragments;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import adri.suys.un_mutescan.R;
import adri.suys.un_mutescan.model.Counterpart;
import adri.suys.un_mutescan.presenter.EventStatPresenter;
import adri.suys.un_mutescan.utils.MyFormatter;
import adri.suys.un_mutescan.utils.UnMuteDataHolder;
import adri.suys.un_mutescan.viewinterfaces.EventStatViewInterface;

public class EventStatFragment extends Fragment implements EventStatViewInterface {

    private TextView eventName, eventRoom, eventDate, totalTicket, soldTicket, horizontalBarChartTitle, pieChartTicketTitle, pieChartMoneyTitle;
    private ProgressBar progressBar;
    private EventStatPresenter presenter;
    private PieChart pieChart, cpTicketPieChart, cpMoneyPieChart;
    private HorizontalBarChart horizontalBarChart;
    private View view;
    private Map<Counterpart, Integer> map;
    private boolean isDarkTheme;

    public EventStatFragment(){
        // required
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_event_stat, container, false);
        setViewElements(view);
        this.presenter = new EventStatPresenter(this);
        map = presenter.getDetailsOfSellCp();
        setSellPieChart();
        setHorizontalBarChart();
        setCpSellPieChart();
        setCpMoneyPieChart();
        return view;
    }


    @Override
    public void onResume(){
        super.onResume();
        setSellPieChart();
        setHorizontalBarChart();
        setCpSellPieChart();
        setCpMoneyPieChart();
    }


    public void displayEvent(String name, Date date, int totalTicket, int soldTicket, float presalePercentage){
        progressBar.setVisibility(View.GONE);
        this.eventName.setText(name);
        String totalTicketStr = getResources().getString(R.string.ticket_total, totalTicket);
        String soldTicketStr = getResources().getString(R.string.ticket_sold, soldTicket, presalePercentage);
        if (date != null){
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
            this.eventDate.setText(df.format(date));
        }
        this.totalTicket.setText(totalTicketStr);
        this.soldTicket.setText(soldTicketStr);
    }

    /////////////////////
    // private methods //
    /////////////////////

    private void setViewElements(View v){
        eventName = v.findViewById(R.id.stat_event_name);
        eventDate = v.findViewById(R.id.stat_event_date);
        totalTicket = v.findViewById(R.id.stat_event_total_ticket);
        soldTicket = v.findViewById(R.id.stat_event_total_ticket_sold);
        progressBar = v.findViewById(R.id.progressBar_stat);
        progressBar.setVisibility(View.VISIBLE);
        pieChart = v.findViewById(R.id.piechart);
        cpTicketPieChart = v.findViewById(R.id.piechart_ticket);
        cpMoneyPieChart = v.findViewById(R.id.piechart_money);
        horizontalBarChart = v.findViewById(R.id.horizontal_barchart);
        horizontalBarChartTitle = v.findViewById(R.id.horizontal_barchart_title);
        pieChartTicketTitle = v.findViewById(R.id.piechart_title_ticket);
        pieChartMoneyTitle = v.findViewById(R.id.piechart_title_money);
    }

    private void setHorizontalBarChart(){
        BarDataSet dataSet = new BarDataSet(getNbOfCashBcPayment(), view.getResources().getString(R.string.progress_state_payment_mode));
        dataSet.setColors(UnMuteDataHolder.getColorsArrayForHorizontalBarchart(getContext()));
        BarData data = new BarData(dataSet);

        if (isDarkTheme = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(PreferenceFragment.KEY_DARK_MODE, true)){
            horizontalBarChart.getXAxis().setTextColor(Color.parseColor("#FFFFFF"));
            horizontalBarChart.getAxisLeft().setTextColor(Color.parseColor("#FFFFFF"));
            horizontalBarChart.getAxisRight().setTextColor(Color.parseColor("#FFFFFF"));}
        else{
            horizontalBarChart.getXAxis().setTextColor(Color.parseColor("#000000"));
            horizontalBarChart.getAxisLeft().setTextColor(Color.parseColor("#000000"));
            horizontalBarChart.getAxisRight().setTextColor(Color.parseColor("#000000"));
        }
        this.horizontalBarChart.setData(data);
        this.horizontalBarChart.animateXY(2000, 2000);
        this.horizontalBarChart.getDescription().setEnabled(false);
        this.horizontalBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getXAxisValue()));
        this.horizontalBarChart.getXAxis().setGranularity(1f);
        this.horizontalBarChart.setDrawValueAboveBar(false);
        this.horizontalBarChart.getAxisLeft().setAxisMaximum(100);
        this.horizontalBarChart.getAxisLeft().setAxisMinimum(0);
        this.horizontalBarChart.getAxisRight().setAxisMaximum(100);
        this.horizontalBarChart.getAxisRight().setAxisMinimum(0);
        this.horizontalBarChart.getLegend().setEnabled(false);
        if (presenter.getTotalSoldOnSite() == 0){
            displayHorizontalBarChart(false);
        } else {
            displayHorizontalBarChart(true);
        }
    }

    private void displayHorizontalBarChart(boolean displayed){
        if (displayed){
            horizontalBarChart.setVisibility(View.VISIBLE);
            horizontalBarChartTitle.setVisibility(View.VISIBLE);
        } else {
            horizontalBarChart.setVisibility(View.GONE);
            horizontalBarChartTitle.setVisibility(View.GONE);
        }
    }

    private void setSellPieChart(){
        List<PieEntry> entries = getValuesList();
        PieDataSet pieDataSet = new PieDataSet(entries, "");
        PieData pieData = new PieData(pieDataSet);
        pieDataSet.setColors(UnMuteDataHolder.getColorsArrayForStatPieChart(getContext()));
        pieDataSet.setValueFormatter(new MyFormatter());
        this.pieChart.setData(pieData);
        this.pieChart.animateY(2000);
        this.pieChart.setDrawEntryLabels(false);
        this.pieChart.getDescription().setEnabled(false);

        Legend legend = pieChart.getLegend();
        legend.setTextSize(13f);
        legend.setWordWrapEnabled(true);
        legend.setTextColor(R.color.white);

        if (isDarkTheme = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(PreferenceFragment.KEY_DARK_MODE, true)){
            legend.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else{
            legend.setTextColor(Color.parseColor("#000000"));
        }

    }

    private void setCpSellPieChart(){
        List<PieEntry> entries = getNbTicketsPerCpStat();
        PieDataSet pieDataSet = new PieDataSet(entries, "");
        PieData pieData = new PieData(pieDataSet);
        pieDataSet.setColors(UnMuteDataHolder.getColorsArrayForCounterpartStat(getContext()));
        pieDataSet.setValueFormatter(new MyFormatter());
        cpTicketPieChart.setData(pieData);
        cpTicketPieChart.animateY(2000);
        cpTicketPieChart.setDrawEntryLabels(false);
        Legend legend = cpTicketPieChart.getLegend();
        legend.setTextSize(13f);
        legend.setWordWrapEnabled(true);
        legend.setTextColor(R.color.white);

        if (isDarkTheme = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(PreferenceFragment.KEY_DARK_MODE, true)){
            legend.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else{
            legend.setTextColor(Color.parseColor("#000000"));
        }

        cpTicketPieChart.getDescription().setEnabled(false);
        pieChartTicketTitle.setVisibility(presenter.getTotalSold() > 0 ? View.VISIBLE : View.GONE);
        cpTicketPieChart.setVisibility(presenter.getTotalSold() > 0 ? View.VISIBLE : View.GONE);
    }

    private void setCpMoneyPieChart(){
        List<PieEntry> entries = getMoneyPerCpStat();
        PieDataSet pieDataSet = new PieDataSet(entries, "");
        PieData pieData = new PieData(pieDataSet);
        pieDataSet.setColors(UnMuteDataHolder.getColorsArrayForCounterpartStat(getContext()));
        pieDataSet.setValueFormatter(new MyFormatter());
        cpMoneyPieChart.setData(pieData);
        cpMoneyPieChart.animateY(2000);
        cpMoneyPieChart.setDrawEntryLabels(false);
        Legend legend = cpMoneyPieChart.getLegend();
        legend.setTextSize(13f);
        legend.setWordWrapEnabled(true);

        if (isDarkTheme = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(PreferenceFragment.KEY_DARK_MODE, true)){
            legend.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else{
            legend.setTextColor(Color.parseColor("#000000"));
        }

        cpMoneyPieChart.getDescription().setEnabled(false);
        pieChartMoneyTitle.setVisibility(presenter.getTotalSold() > 0 ? View.VISIBLE : View.GONE);
        cpMoneyPieChart.setVisibility(presenter.getTotalSold() > 0 ? View.VISIBLE : View.GONE);
    }

    private List<PieEntry> getValuesList(){
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(presenter.getTotalScanned(), view.getResources().getString(R.string.pie_chart_scanned)));
        entries.add(new PieEntry(presenter.getTotalToBeScanned(), view.getResources().getString(R.string.pie_chart_to_be_scanned)));
        entries.add(new PieEntry(presenter.getTotalSoldOnSite(), view.getResources().getString(R.string.pie_chart_sold_on_site)));
        entries.add(new PieEntry(presenter.getTotalRemaining(), view.getResources().getString(R.string.pie_chart_remaining)));
        return entries;
    }

    private List<BarEntry> getNbOfCashBcPayment(){
        double x = ((double) presenter.getTotalPaidInCash()) / presenter.getTotalSoldOnSite();
        double cashPercentage = x * 100;
        double cardPercentage = 100 - cashPercentage;
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, (float) cashPercentage));
        entries.add(new BarEntry(1, (float) cardPercentage));
        return entries;
    }

    private List<String> getXAxisValue(){
        List<String> xAxisValues = new ArrayList<>();
        String cashLabel = view.getResources().getString(R.string.horizontalchart_label_cash, presenter.getTotalPaidInCash(), presenter.getTotalSoldOnSite());
        xAxisValues.add(cashLabel);
        int totalPaidWithCard = presenter.getTotalSoldOnSite() - presenter.getTotalPaidInCash();
        String cbLabel = view.getResources().getString(R.string.horizontalchart_label_cb, totalPaidWithCard, presenter.getTotalSoldOnSite());
        xAxisValues.add(cbLabel);
        return xAxisValues;
    }

    private List<PieEntry> getNbTicketsPerCpStat(){
        List<PieEntry> nbTicketsPerCpStat = new ArrayList<>();
        Iterator it = map.entrySet().iterator();
        int i = 1;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String cpName = ((Counterpart) pair.getKey()).getName();
            nbTicketsPerCpStat.add(new PieEntry((Integer) pair.getValue(), cpName));
            i++;
        }
        return nbTicketsPerCpStat;
    }

    private List<PieEntry> getMoneyPerCpStat(){
        List<PieEntry> moneyPerCpStat = new ArrayList<>();
        Iterator it = map.entrySet().iterator();
        int i = 1;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Counterpart cp = (Counterpart) pair.getKey();
            float money = (float) (cp.getPrice() * (Integer) pair.getValue());
            moneyPerCpStat.add(new PieEntry(money, cp.getName()));
            i++;
        }
        return moneyPerCpStat;
    }

}
