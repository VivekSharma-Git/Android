package com.example.viveksharma.myapplicationv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   RecyclerView mrv;
   RecyclerView.LayoutManager rlm;
   private List<Student> studentList = new ArrayList<Student>();
   StudentAdapter sa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mrv =(RecyclerView)findViewById(R.id.rv);

        rlm =new LinearLayoutManager(this);
        mrv.setLayoutManager(rlm);

        sa = new StudentAdapter(studentList);
        mrv.setAdapter(sa);

        prepareStudentData();
    }
    public void prepareStudentData(){
        for(int i=1;i<30;i++) {
            Student s = new Student("Test", 123*i, 2*i);
            studentList.add(s);
        }
        sa.notifyDataSetChanged();
    }

}
