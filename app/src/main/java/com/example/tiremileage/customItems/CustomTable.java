package com.example.tiremileage.customItems;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.example.tiremileage.R;

import java.util.ArrayList;
import java.util.List;

public class CustomTable extends LinearLayout {

    List<TextView> columnNamesViewList = new ArrayList<>();

    public CustomTable(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public CustomTable(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs, 0);
    }

    public CustomTable(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void clearData(){
        TableLayout table = findViewById(R.id.table);
        table.removeAllViews();
    }

    private void init(Context context, AttributeSet attributeSet, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.custom_table, this, true);
        LinearLayout columnNamesLayout = findViewById(R.id.column_names);

        setDrawableColor(ContextCompat.getDrawable(getContext(),R.drawable.table_separator),R.color.white);
        columnNamesLayout.setDividerDrawable(ContextCompat.getDrawable(getContext(),R.drawable.table_separator));
        TypedArray attributes = context.obtainStyledAttributes(attributeSet, R.styleable.CustomTable, defStyleAttr, 0);
            try {
                CharSequence[] columnNames = attributes.getTextArray(R.styleable.CustomTable_table_columns);
            if (columnNames != null)
            for (CharSequence columnName : columnNames) {
                TextView textView = initTextView(context, columnName.toString(), R.color.silver, R.color.blue);
                columnNamesLayout.addView(textView);
                columnNamesViewList.add(textView);
            } else {
                TextView textView = initTextView(context, "empty", R.color.silver, R.color.blue);
                columnNamesLayout.addView(textView);
                columnNamesViewList.add(textView);
            }
        } finally {
                attributes.recycle();
            }
    }

    public void addRaw(List<String> rawData){
        TableRow tableRow = new TableRow(getContext());
        tableRow.setShowDividers(TableRow.SHOW_DIVIDER_MIDDLE);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));

        setDrawableColor(ContextCompat.getDrawable(getContext(),R.drawable.table_separator),R.color.black);
        tableRow.setDividerDrawable(ContextCompat.getDrawable(getContext(),R.drawable.table_separator));

        for (int i = 0; i < rawData.size();i++){
            addTextViewOnTableRaw(tableRow, rawData.get(i), i);
        }

        TableLayout table = findViewById(R.id.table);
        table.addView(tableRow);
    }

    private void addTextViewOnTableRaw(TableRow tableRow, String string, int num){
        TextView textView = initTextView(
                getContext(),
                string,
                R.color.black,
                R.color.white);
        tableRow.addView(textView, num);
    }

    private TextView initTextView(Context context, String text, int textColor, int backColor){
        TextView item = new TextView(context);
        item.setText(text);
        item.setBackgroundColor(
                ContextCompat.getColor(context,backColor));
        item.setPadding(30,30,30,30);
        item.setTextSize(16f);
        item.setTextColor(
                ContextCompat.getColor(context,textColor));
        return item;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (columnNamesViewList.size() != 0) {
            normalize(columnNamesViewList, findViewById(R.id.table));
        }
    }

    private void setDrawableColor(Drawable drawable, int Res){
        if (drawable instanceof ShapeDrawable) {
            ((ShapeDrawable)drawable).getPaint().setColor(ContextCompat.getColor(getContext(),Res));
        } else if (drawable instanceof GradientDrawable) {
            ((GradientDrawable)drawable).setColor(ContextCompat.getColor(getContext(),Res));
        } else if (drawable instanceof ColorDrawable) {
            ((ColorDrawable)drawable).setColor(ContextCompat.getColor(getContext(),Res));
        }
    }

    private void normalize(List<TextView> titles, TableLayout tires){
        if (tires.getChildCount() == 0){
            return;
        }
        TableRow tableRow = (TableRow) tires.getChildAt(0);
        for (int i = 0; i < titles.size(); i++) {
            TextView rowTextView = (TextView) tableRow.getChildAt(i);
            if (titles.get(i).getMeasuredWidth()>rowTextView.getMeasuredWidth()){
                rowTextView.setLayoutParams(new TableRow.LayoutParams(titles.get(i).getMeasuredWidth(),TableRow.LayoutParams.WRAP_CONTENT));
            } else {
                titles.get(i).setLayoutParams(new LinearLayout.LayoutParams(rowTextView.getMeasuredWidth(),LinearLayout.LayoutParams.WRAP_CONTENT));
            }
        }
    }
}
