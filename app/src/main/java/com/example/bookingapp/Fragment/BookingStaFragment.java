package com.example.bookingapp.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.bookingapp.Adapter.RoomBookingAdapter;
import com.example.bookingapp.Model.Room;
import com.example.bookingapp.Model.RoomBooking;
import com.github.mikephil.charting.charts.BarChart;
import com.example.bookingapp.R;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookingStaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookingStaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button btntodayrev,btntodaybook,btntodaypaid,
    btnmonthrev,btnmonthbook,btnmonthpaid;
    RecyclerView rcv;
    ArrayList<RoomBooking> roomBookings;
    ScrollView scrollView1;
    ArrayList<BarEntry> barEntries;
    TextView txttotal,txtrev;
    BarChart barChart;
    RoomBookingAdapter roomBookingAdapter;
    int btn1 = 0,max = 0,btn2 = 0;
    double revenue = 0;
    FirebaseFirestore db;
    boolean isBtn1Click = true;

    public BookingStaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookingStaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookingStaFragment newInstance(String param1, String param2) {
        BookingStaFragment fragment = new BookingStaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_sta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initView(view);
        setButton1click();
        setButton2click();
    }
    private void initView(View view)
    {
        barEntries = new ArrayList<>();
        roomBookings = new ArrayList<>();
        roomBookingAdapter = new RoomBookingAdapter(roomBookings,getContext(),R.layout.booking_item);
        btntodaybook = view.findViewById(R.id.adminbooksta_todaybook);
        btntodaypaid = view.findViewById(R.id.adminbooksta_todaypaid);
        btntodayrev = view.findViewById(R.id.adminbooksta_todayrev);
        btnmonthbook = view.findViewById(R.id.adminbooksta_btnmonthlybooking);
        btnmonthrev = view.findViewById(R.id.adminbooksta_btnmonthlyrev);
        scrollView1 = view.findViewById(R.id.adminbooksta_scroll1);
        btnmonthpaid = view.findViewById(R.id.adminbooksta_btnmonthlypaid);
        txttotal = view.findViewById(R.id.adminbooksta_resulttxt);
        rcv = view.findViewById(R.id.adminbooksta_rcv);
        rcv.setNestedScrollingEnabled(false);
        txtrev = view.findViewById(R.id.adminbooksta_txtrev);
        barChart = view.findViewById(R.id.adminbooksta_barchart);
    }
    private void setButton1click()
    {
        btntodaybook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBtn1Click = true;
                if(btn1 != 1)
                    btn1 = 1;
                checkButton1click();
                setRcv("bookingday", RoomBooking.RoomBookingState.BOOKING.value);
            }
        });
        btntodaypaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBtn1Click = true;
                if(btn1 != 2)
                    btn1 = 2;
                checkButton1click();
                setRcv("accdate",  RoomBooking.RoomBookingState.PAID.value);

            }
        });
        btntodayrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBtn1Click = true;
                if(btn1 != 3)
                    btn1 = 3;
                checkButton1click();
                setRcv("accdate", RoomBooking.RoomBookingState.PAID.value);
            }
        });
    }
    private void setButton2click()
    {
        btnmonthrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn2 != 1)
                    btn2 = 1;
                isBtn1Click = false;
                checkButton1click();
                List<Integer> i = new ArrayList<>();
                i.add(RoomBooking.RoomBookingState.PAID.value);
                i.add(RoomBooking.RoomBookingState.ENDBOOKING.value);
                setBarChart("RoomBooking","accdate",i);
            }
        });
        btnmonthbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn2 != 2)
                    btn2 = 2;
                isBtn1Click = false;
                checkButton1click();
                List<Integer> i = new ArrayList<>();
                i.add(RoomBooking.RoomBookingState.PAID.value);
                i.add(RoomBooking.RoomBookingState.ENDRATING.value);
                i.add(RoomBooking.RoomBookingState.BOOKING.value);
                setBarChart("RoomBooking","bookingday",i);
            }
        });
        btnmonthpaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn2 != 3)
                    btn2 = 3;
                isBtn1Click = false;
                checkButton1click();
                List<Integer> i = new ArrayList<>();
                i.add(RoomBooking.RoomBookingState.PAID.value);
                i.add(RoomBooking.RoomBookingState.ENDBOOKING.value);
                setBarChart("RoomBooking","accdate",i);
            }
        });
    }
    private void setBarChart(String collection, String field,List<Integer> status)
    {
        barEntries.clear();
        barChart.getAxisRight().setDrawLabels(false);
        Query query = db.collection(collection).whereIn("status",status);
        if(btn2 == 1)
        {
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    revenue = 0;
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                    {
                        RoomBooking roomBooking = queryDocumentSnapshot.toObject(RoomBooking.class);
                        revenue += roomBooking.getTotalprice();
                    }
                }
            });
        }
        else
        {
        db.collection(collection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    max = task.getResult().size();
            }
        });}

        for(int i = 1;i<=12;i++)
        {
            addEntry(collection,field,status,i);
        }
    }
    private void addEntry(String collection, String field, List<Integer> status, int month)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,month);
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Timestamp ts2 = new Timestamp(calendar.getTime());
        calendar.set(calendar.get(Calendar.YEAR),month,1);
        Timestamp ts1 = new Timestamp(calendar.getTime());
        db.collection(collection).
                whereGreaterThanOrEqualTo(field,ts1)
                .whereLessThanOrEqualTo(field,ts2)
                .whereIn("status",status)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            if(btn2 == 1)
                            {
                                double monthrev = 0;
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                                {
                                    RoomBooking roomBooking = queryDocumentSnapshot.toObject(RoomBooking.class);
                                    monthrev += roomBooking.getTotalprice();
                                }
                                barEntries.add(new BarEntry(month,(float) monthrev));
                            }
                            else
                                barEntries.add(new BarEntry(month,task.getResult().size()));
                            YAxis yAxis = barChart.getAxisLeft();
                            yAxis.setAxisMaximum(0f);
                            if(btn2 != 1)
                                yAxis.setAxisMaximum(max);
                            else
                                yAxis.setAxisMaximum((float) revenue);
                            yAxis.setAxisLineWidth(2f);
                            yAxis.setAxisLineColor(Color.BLACK);
                            yAxis.setLabelCount(5);
                            yAxis.setTextSize(16f);

                            BarDataSet barDataSet = new BarDataSet(barEntries,"Month");
                            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                            barDataSet.setValueTextSize(16f);

                            BarData barData = new BarData(barDataSet);
                            barChart.setData(barData);
                            barChart.setFitBars(true);
                            barChart.getDescription().setEnabled(false);
                            barChart.invalidate();
                            ArrayList<String> months = new ArrayList<>(Arrays.asList("1","2"
                                    ,"3","4","5","6"
                                    ,"7","8","9","10"
                                    ,"11","12"));
                            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
                            barChart.getXAxis().setTextSize(16f);
                            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                            barChart.getXAxis().setGranularity(1f);
                            barChart.getXAxis().setGranularityEnabled(true);
                        }
                    }
                });
    }
    private void checkButton1click()
    {
        if(isBtn1Click)
        {
            if(btn1 == 3)
            {
                txtrev.setVisibility(View.VISIBLE);
                scrollView1.setVisibility(View.GONE);
            }
            else
            {
                txtrev.setVisibility(View.GONE);
                scrollView1.setVisibility(View.VISIBLE);
            }
            barChart.setVisibility(View.GONE);
        }
        else
        {
            txtrev.setVisibility(View.GONE);
            scrollView1.setVisibility(View.GONE);
            barChart.setVisibility(View.VISIBLE);
        }
    }
    private void setRcv(String field,int status)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Timestamp.now().toDate());
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE),0,0,0);
        Date date = calendar.getTime();
        Log.i("Date",date.toString());
        db.collection("RoomBooking").
                whereEqualTo("status",status)
                .whereGreaterThanOrEqualTo(field,new Timestamp(date))
                .whereLessThanOrEqualTo(field,Timestamp.now())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            revenue = 0;
                            roomBookings.clear();
                            Log.i("revenue",Integer.toString(task.getResult().size()));
                            txttotal.setText("Total: "+ task.getResult().size());
                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                            {
                                RoomBooking roomBooking = queryDocumentSnapshot.toObject(RoomBooking.class);
                                roomBookings.add(roomBooking);
                                revenue += roomBooking.getTotalprice();
                            }
                            txtrev.setText("TODAY REVENUE: " + revenue + "$");
                            rcv.setAdapter(roomBookingAdapter);
                            rcv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                        }
                    }
                });
    }
}