package com.example.viveksharma.recyclerviewapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv ;
    RecyclerView.LayoutManager rlm;
    EmployeeAdapter ea;
    List<Employee> employeeList = new ArrayList<Employee>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = (RecyclerView)findViewById(R.id.rcv);
        rlm = new LinearLayoutManager(this);
        rv.setLayoutManager(rlm);
        ea = new EmployeeAdapter(employeeList);
        rv.setAdapter(ea);
        prepareEmployeeData();
    }
    public void prepareEmployeeData(){
        for(int i =0;i<30;i++)
        {
            Employee e = new Employee(i, "Test"+i, "TestDesignation"+i);
            employeeList.add(e);
        }
        ea.notifyDataSetChanged();

    }
}
