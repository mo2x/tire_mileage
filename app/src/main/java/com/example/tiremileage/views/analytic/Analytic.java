package com.example.tiremileage.views.analytic;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.tiremileage.R;
import com.example.tiremileage.databinding.FragmentAnalyticsBinding;
import com.example.tiremileage.room.Entities.Monitor;
import com.example.tiremileage.views.tiregrid.TireTableViewModel;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class Analytic extends Fragment {

    FragmentAnalyticsBinding binding;
    AnalyticViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(
                requireActivity()).get(AnalyticViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAnalyticsBinding.inflate(inflater, container, false);
        viewModel.updateMonitor();
        viewModel.monitor.observe(getViewLifecycleOwner(), monitor -> {
            ArrayList<Entry> temp = new ArrayList<>();
            ArrayList<Entry> kpa = new ArrayList<>();
            ArrayList<Entry> depth = new ArrayList<>();
            for (int i = 0; i < monitor.size(); i++){
                Monitor currentMonitor = monitor.get(i);
                temp.add(new Entry(currentMonitor.date.getTime(),(float) currentMonitor.temperature));
                kpa.add(new Entry(currentMonitor.date.getTime(),(float) currentMonitor.kpa));
                depth.add(new Entry(currentMonitor.date.getTime(),(float) currentMonitor.tread_depth));
            }
            LineDataSet lineDataSetTemp = new LineDataSet(temp, "temperature");
            LineDataSet lineDataSetKpa = new LineDataSet(kpa, "pressure");
            LineDataSet lineDataSetDepth = new LineDataSet(depth, "tire depth");
//            LineData data = new LineData();
//            data.addDataSet(lineDataSetTemp);
//            data.addDataSet(lineDataSetKpa);
//            data.addDataSet(lineDataSetDepth);
            LineData lineDataTemp = new LineData();
            lineDataTemp.addDataSet(lineDataSetTemp);
            binding.chartTemp.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());
            binding.chartTemp.setData(lineDataTemp);

            LineData lineDataKpa = new LineData();
            lineDataKpa.setValueFormatter(new LineChartXAxisValueFormatter());
            lineDataKpa.addDataSet(lineDataSetKpa);
            binding.chartKpa.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());
            binding.chartKpa.setData(lineDataKpa);

            LineData lineDataDepth = new LineData();
            lineDataDepth.setValueFormatter(new LineChartXAxisValueFormatter());
            lineDataDepth.addDataSet(lineDataSetDepth);
            binding.chartDepth.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());
            binding.chartDepth.setData(lineDataDepth);

            binding.chartDepth.invalidate();
            binding.chartTemp.invalidate();
            binding.chartKpa.invalidate();
        });
        binding.button3.setOnClickListener(v -> {
            viewModel.updateMonitor();
        });
        return binding.getRoot();

    }
}