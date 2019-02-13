package engineering.badve.badveengineering.reports;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import engineering.badve.badveengineering.R;
import engineering.badve.badveengineering.classes.MyApplication;
import engineering.badve.badveengineering.classes.T;
import engineering.badve.badveengineering.classes.V;

public class DeleteQueryActivity extends AppCompatActivity {

    @BindView(R.id.editText2)
    EditText editText2;

    @BindView(R.id.editText3)
    EditText editText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_query);

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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



    public void deleteData(View view)
    {

        if(!V.validateEmptyField(DeleteQueryActivity.this,editText2,"Warning ! empty field"))
        {
            return;
        }
        MyApplication.db.fireQuery(editText2.getText().toString());
        T.t(DeleteQueryActivity.this,"Success");
    }

    public void insertData(View view)
    {

        if(!V.validateEmptyField(DeleteQueryActivity.this,editText3,"Warning ! empty field"))
        {
            return;
        }
        MyApplication.db.fireQuery(editText3.getText().toString());
        T.t(DeleteQueryActivity.this,"Success");
    }

    public void insertAttendance(View view)
    {
        MyApplication.db.fireQuery("insert into attendance(rfidNumber,bnumber,intime,outtime,name,sstatus,time) values " +
                "('9119825410250','1114190122','15-02-2018 06:57:00','0','A','1','15-02-2018')");

    }
}
