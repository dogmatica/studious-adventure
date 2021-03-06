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
import com.example.projectguru.data.Resource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.List;

public class ResourceEdit extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static String LOG_TAG = "ResourceEditActivityLog";
    MainDatabase db;
    EditText resourceNamePlainText;
    EditText resourcePhone;
    EditText resourceEmail;
    Spinner spinner;
    FloatingActionButton resourceSaveButton;
    int resourceId;
    Intent intent;
    Resource selectedResource;

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
        setContentView(R.layout.activity_resource_edit);
        setTitle("Add or Edit Resource");
        resourceNamePlainText = findViewById(R.id.resourceNamePlainText);
        resourcePhone = findViewById(R.id.resourcePhone);
        resourceEmail = findViewById(R.id.resourceEmail);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        resourceSaveButton = findViewById(R.id.resourceSaveButton);
        db = MainDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        resourceId = intent.getIntExtra("resourceId", -1);
        selectedResource = db.resourceDao().getResource(resourceId);

        //Query the database and update current layout with appropriate data:

        updateViews();

        resourceSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempName = String.valueOf(resourceNamePlainText.getText());
                if (nameCheck(tempName) == true) {
                    //Gathering field entries and inserting into Resource table
                    try {
                        //First the Resource is created and inserted
                        Resource newResource = new Resource();
                        newResource.setResource_id(resourceId);
                        newResource.setResource_name(String.valueOf(resourceNamePlainText.getText()));
                        newResource.setResource_phone(String.valueOf(resourcePhone.getText()));
                        newResource.setResource_email(String.valueOf(resourceEmail.getText()));
                        newResource.setResource_type(String.valueOf(spinner.getSelectedItem()));
                        db.resourceDao().updateResource(newResource);
                        Intent intent = new Intent(getApplicationContext(), ResourceManage.class);
                        List<Resource> resourceList = db.resourceDao().getResourceList();
                        intent.putExtra("resourceList", (Serializable) resourceList);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (nameCheck(tempName) == false) {
                    Toast.makeText(getApplicationContext(), "Name cannot be blank.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    //Query the database and update current layout with appropriate data:

    private void updateViews() {
        if (selectedResource != null) {
            Log.d(WorkUnitEdit.LOG_TAG, "selected Resource is not null");
            resourceNamePlainText.setText(selectedResource.getResource_name());
            resourcePhone.setText(selectedResource.getResource_phone());
            resourceEmail.setText(selectedResource.getResource_email());
        } else {
            Log.d(WorkUnitEdit.LOG_TAG, "selected Resource is null");
            selectedResource = new Resource();
        }
    }

    private Boolean nameCheck(String name) {
        int length = name.length();
        if (length < 1) {
            return false;
        } else {
            return  true;
        }
    }

    private Boolean phoneCheck(String phone) {
        int length = phone.length();
        char[] phoneArray = phone.toCharArray();
        int errCount = 0;
        for (int i = 0; i < length; i++) {
            char digit = phoneArray[i];
            if (Character.isDigit(digit) | Character.isLetter('-')) {
                continue;
            } else {
                errCount++;
            }
        }
        if (errCount > 0) {
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