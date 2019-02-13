package engineering.badve.badveengineering.reports;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import engineering.badve.badveengineering.R;
import engineering.badve.badveengineering.classes.MyApplication;
import engineering.badve.badveengineering.classes.T;
import engineering.badve.badveengineering.database.S;
import engineering.badve.badveengineering.reportadapter.AttendanceAdapter;
import engineering.badve.badveengineering.reportadapter.EmployeeInfoAdapter;

public class EmployeeDetailsActivity extends AppCompatActivity {

    @BindView(R.id.reportsRecyclerView)
    RecyclerView reportsRecyclerView;

    private EmployeeInfoAdapter bookingDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(EmployeeDetailsActivity.this));
        bookingDetailsAdapter = new EmployeeInfoAdapter(EmployeeDetailsActivity.this);
        reportsRecyclerView.setAdapter(bookingDetailsAdapter);

        getDataa();

    }

    private void getDataa()
    {
        ArrayList<EmployeeInfo> detailsInformationArrayList = S.getEmployeeDetails();

        if(detailsInformationArrayList.isEmpty())
        {
            T.t(EmployeeDetailsActivity.this,"No data found");
        }
        else
        {
            bookingDetailsAdapter.setCellId(detailsInformationArrayList,EmployeeDetailsActivity.this);
            bookingDetailsAdapter.notifyDataSetChanged();
        }
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


}
