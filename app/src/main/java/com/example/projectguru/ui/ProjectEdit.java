package com.example.projectguru.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectguru.R;
import com.example.projectguru.data.MainDatabase;
import com.example.projectguru.data.Project;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectEdit extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static String LOG_TAG = "ProjectEditActivityLog";
    MainDatabase db;
    EditText projectNamePlainText;
    EditText projectStartDate;
    EditText projectEndDate;
    Spinner spinner;
    FloatingActionButton projectSaveButton;
    int projectId;
    SimpleDateFormat formatter;
    Intent intent;
    Project selectedProject;
    Date newStartDate;
    Date newEndDate;

    //Inflation of hidden menu on action bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    //Actions related to hidden menu selection

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //When "Home" is selected:
            case R.id.home:
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_edit);
        setTitle("Add or Edit Project");
        projectNamePlainText = findViewById(R.id.projectNamePlainText);
        projectStartDate = findViewById(R.id.projectStartDate);
        projectEndDate = findViewById(R.id.projectEndDate);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.statuses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        projectSaveButton = findViewById(R.id.projectSaveButton);
        db = MainDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        projectId = intent.getIntExtra("projectId", -1);
        selectedProject = db.projectDao().getProject(projectId);
        formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        //Query the database and update current layout with appropriate data:

        updateViews();

        projectSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempName = String.valueOf(projectNamePlainText.getText());
                int length = tempName.length();
                if (validateName(length) == false) {
                    Toast.makeText(getApplicationContext(), "Name cannot be blank.", Toast.LENGTH_LONG).show();
                } else {
                    //Gathering field entries and inserting into Project table
                    try {
                        //First the Project is created and inserted
                        Project newProject = new Project();
                        newStartDate = formatter.parse(String.valueOf(projectStartDate.getText()));
                        newEndDate = formatter.parse(String.valueOf(projectEndDate.getText()));
                        newProject.setProject_id(projectId);
                        newProject.setProject_name(String.valueOf(projectNamePlainText.getText()));
                        newProject.setProject_start(newStartDate);
                        newProject.setProject_end(newEndDate);
                        newProject.setProject_status(String.valueOf(spinner.getSelectedItem()));
                        db.projectDao().updateProject(newProject);
                        Intent intent = new Intent(getApplicationContext(), ProjectsList.class);
                        intent.putExtra("projectId", projectId);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    //Query the database and update current layout with appropriate data:

    private void updateViews() {
        if (selectedProject != null) {
            Log.d(ProjectEdit.LOG_TAG, "selected Project is not null");
            Date startDate = selectedProject.getProject_start();
            Date endDate = selectedProject.getProject_end();
            String tempStart = formatter.format(startDate);
            String tempEnd = formatter.format(endDate);
            projectStartDate.setText(tempStart);
            projectEndDate.setText(tempEnd);
            projectNamePlainText.setText(selectedProject.getProject_name());
        } else {
            Log.d(ProjectEdit.LOG_TAG, "selected Project is null");
            selectedProject = new Project();
        }
    }

    protected Boolean validateName(int length) {
        if (length < 1) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}