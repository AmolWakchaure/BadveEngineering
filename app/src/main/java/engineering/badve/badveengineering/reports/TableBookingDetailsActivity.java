package engineering.badve.badveengineering.reports;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import engineering.badve.badveengineering.activity.AddCellActivity;
import engineering.badve.badveengineering.adapter.CellIdAdapter;
import engineering.badve.badveengineering.classes.MyApplication;
import engineering.badve.badveengineering.classes.T;
import engineering.badve.badveengineering.dashboard.HomeActivity;
import engineering.badve.badveengineering.database.S;
import engineering.badve.badveengineering.pojo.BookingDetailsInformation;
import engineering.badve.badveengineering.reportadapter.BookingDetailsAdapter;

public class TableBookingDetailsActivity extends AppCompatActivity {

    @BindView(R.id.reportsRecyclerView)
    RecyclerView reportsRecyclerView;

    private BookingDetailsAdapter bookingDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_booking_details);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(TableBookingDetailsActivity.this));
        bookingDetailsAdapter = new BookingDetailsAdapter(TableBookingDetailsActivity.this);
        reportsRecyclerView.setAdapter(bookingDetailsAdapter);

        getDataa();

    }

    private void getDataa()
    {
        ArrayList<BookingDetailsInformation> detailsInformationArrayList = S.getBookingDetails();

        if(detailsInformationArrayList.isEmpty())
        {
            T.t(TableBookingDetailsActivity.this,"No data found");
        }
        else
        {
            bookingDetailsAdapter.setCellId(detailsInformationArrayList,TableBookingDetailsActivity.this);
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

    public void deleteDataId(final String id)
    {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_dialog, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Delete");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {


                MyApplication.db.deleteBookingData(id);
                T.t(MyApplication.context,"Deleted");
                getDataa();
                dialog.dismiss();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();





    }

    public void updateDataId(String id)
    {


        showChangeLangDialog(id);
    }
    public void showChangeLangDialog(String id)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.query_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.editText);
        edt.setText("update booking set outcount = '0' where id = "+id+"");

        dialogBuilder.setTitle("Update");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {


                MyApplication.db.updateOutCount(edt.getText().toString());
                T.t(MyApplication.context,"Updated");
                //do something with edt.getText().toString();
                getDataa();
                dialog.dismiss();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
}
