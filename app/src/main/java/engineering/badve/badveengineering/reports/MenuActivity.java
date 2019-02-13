package engineering.badve.badveengineering.reports;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import engineering.badve.badveengineering.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void deleteQuery(View view) {

        startActivity(new Intent(MenuActivity.this,DeleteQueryActivity.class));
    }

    public void displayBooking(View view) {

        startActivity(new Intent(MenuActivity.this,TableBookingDetailsActivity.class));
    }

    public void displayAttendance(View view) {

        startActivity(new Intent(MenuActivity.this,AttendanceActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add_cell, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void displayEmployee(View view) {

        startActivity(new Intent(MenuActivity.this,EmployeeDetailsActivity.class));
    }
    public void displayShift(View view) {

        startActivity(new Intent(MenuActivity.this,ShiftActivity.class));
    }
}
