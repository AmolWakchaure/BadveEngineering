package engineering.badve.badveengineering.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import engineering.badve.badveengineering.R;
import engineering.badve.badveengineering.classes.BeplLog;
import engineering.badve.badveengineering.classes.MyApplication;
import engineering.badve.badveengineering.classes.V;
import engineering.badve.badveengineering.dashboard.HomeActivity;
import engineering.badve.badveengineering.interfaces.Constants;
import engineering.badve.badveengineering.widgets.MyButton;
import engineering.badve.badveengineering.widgets.MyEditextt;

public class ChangePasswordActivity extends AppCompatActivity {

    @BindView(R.id.oldPasswordEditext)
    MyEditextt oldPasswordEditext;

    @BindView(R.id.newPasswordEditext)
    MyEditextt newPasswordEditext;

    @BindView(R.id.confirmNewPasswordEditext)
    MyEditextt confirmNewPasswordEditext;

    @BindView(R.id.submitButton)
    MyButton submitButton;

    private String PASSWORD_STATUS,OLD_PASSWORDD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null)
        {
            PASSWORD_STATUS = bundle.getString(Constants.PASSWORD_STATUS);
            OLD_PASSWORDD = bundle.getString(Constants.OLD_PASSWORD);

        }

        initialise();
    }

    private void initialise() {

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Change Password");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add_cell, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.submitButton)
    public void onViewClicked()
    {
        if(PASSWORD_STATUS.equals("first"))
        {
            oldPasswordEditext.setVisibility(View.GONE);
            if(!V.validateEmptyField(ChangePasswordActivity.this,newPasswordEditext,"Warning ! enter new password."))
            {
                return;
            }
            if(!V.validateEmptyField(ChangePasswordActivity.this,confirmNewPasswordEditext,"Warning ! enter confirm password."))
            {
                return;
            }
            if(!V.validateConfNewPassword(newPasswordEditext.getText().toString(),confirmNewPasswordEditext.getText().toString()))
            {
                return;
            }
            MyApplication.editor.commit();
            MyApplication.editor.putString(Constants.OLD_PASSWORD,newPasswordEditext.getText().toString());
            MyApplication.editor.putString(Constants.NEW_PASSWORD,newPasswordEditext.getText().toString());
            MyApplication.editor.commit();
            BeplLog.passwordBackup(""+newPasswordEditext.getText().toString());
            startActivity(new Intent(ChangePasswordActivity.this, HomeActivity.class));
            finish();
        }
        else if(PASSWORD_STATUS.equals("second"))
        {
            String oldPassword;
            if(OLD_PASSWORDD.equals("bepl123"))
            {
                oldPassword = "bepl123";
            }
            else
            {
                MyApplication.editor.commit();
                oldPassword = MyApplication.prefs.getString(Constants.OLD_PASSWORD,"0");
                MyApplication.editor.commit();

            }

            if(!V.validateEmptyField(ChangePasswordActivity.this,oldPasswordEditext,"Warning ! enter old password."))
            {
                return;
            }
            if(!V.validateEmptyField(ChangePasswordActivity.this,newPasswordEditext,"Warning ! enter new password."))
            {
                return;
            }
            if(!V.validateEmptyField(ChangePasswordActivity.this,confirmNewPasswordEditext,"Warning ! enter confirm password."))
            {
                return;
            }
            if(!V.validateOldPassword(oldPassword,oldPasswordEditext.getText().toString()))
            {
                return;
            }
            if(!V.validateConfNewPassword(newPasswordEditext.getText().toString(),confirmNewPasswordEditext.getText().toString()))
            {
                return;
            }

            MyApplication.editor.commit();
            MyApplication.editor.putString(Constants.OLD_PASSWORD,newPasswordEditext.getText().toString());
            MyApplication.editor.putString(Constants.NEW_PASSWORD,newPasswordEditext.getText().toString());
            MyApplication.editor.commit();

            BeplLog.passwordBackup(""+newPasswordEditext.getText().toString());
            startActivity(new Intent(ChangePasswordActivity.this, HomeActivity.class));
            finish();
        }


    }
}
