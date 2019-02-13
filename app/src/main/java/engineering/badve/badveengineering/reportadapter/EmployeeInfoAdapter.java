package engineering.badve.badveengineering.reportadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import engineering.badve.badveengineering.R;
import engineering.badve.badveengineering.classes.T;
import engineering.badve.badveengineering.reports.AttendadanceInfo;
import engineering.badve.badveengineering.reports.AttendanceActivity;
import engineering.badve.badveengineering.reports.EmployeeDetailsActivity;
import engineering.badve.badveengineering.reports.EmployeeInfo;

/**
 * Created by Amol on 21-February-18.
 */


public class EmployeeInfoAdapter extends RecyclerView.Adapter<EmployeeInfoAdapter.ViewHolderCarLog> {

    private ArrayList<EmployeeInfo> carlogInformation = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private EmployeeDetailsActivity activity;

    View view ;

    public EmployeeInfoAdapter(Context context)
    {
        layoutInflater = LayoutInflater.from(context);

    }

    public void setCellId(ArrayList<EmployeeInfo> countryInformationsdata,EmployeeDetailsActivity activity) {
        this.carlogInformation = countryInformationsdata;
        this.activity = activity;
        notifyItemRangeChanged(0, countryInformationsdata.size());
    }

    @Override
    public ViewHolderCarLog onCreateViewHolder(ViewGroup parent, int viewType) {

        view = layoutInflater.inflate(R.layout.employee_row, parent, false);
        ViewHolderCarLog viewHolderScheduleholde = new ViewHolderCarLog(view);
        return viewHolderScheduleholde;
    }

    @Override
    public void onBindViewHolder(ViewHolderCarLog holder, int position) {
        try
        {



            EmployeeInfo carLogInformation = carlogInformation.get(position);


            holder.idTextView.setText(carLogInformation.getId());
            holder.rfidNumber.setText(carLogInformation.getRfidNumber());
            holder.bnumber.setText(carLogInformation.getBnumber());
            holder.designation.setText(carLogInformation.getDesignation());
            holder.contractor_code.setText(carLogInformation.getContractor_code());
            holder.cellId.setText(carLogInformation.getCellid());
            holder.date.setText(carLogInformation.getDate());
            holder.sstatus.setText(carLogInformation.getSstatus());


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount()
    {
        return carlogInformation.size();
    }

    class ViewHolderCarLog extends RecyclerView.ViewHolder implements View.OnClickListener{


        private TextView

                idTextView,
                rfidNumber,
                bnumber,
                designation,
                contractor_code,
                cellId,
                date,
                sstatus;


        private LinearLayout
                removeCellId;


        public ViewHolderCarLog(final View itemView)
        {
            super(itemView);

            try
            {



                idTextView = (TextView) itemView.findViewById(R.id.idTextView);
                rfidNumber = (TextView) itemView.findViewById(R.id.rfidNumber);
                bnumber = (TextView) itemView.findViewById(R.id.bnumber);
                designation = (TextView) itemView.findViewById(R.id.designation);
                contractor_code = (TextView) itemView.findViewById(R.id.contractor_code);
                cellId = (TextView) itemView.findViewById(R.id.cellId);
                date = (TextView) itemView.findViewById(R.id.date);
                sstatus = (TextView) itemView.findViewById(R.id.sstatus);



                itemView.setOnClickListener(this);




            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(final View v)
        {
            try
            {




            }
            catch (Exception e)
            {
                e.printStackTrace();
                T.t(v.getContext(),""+e);
            }

        }

    }



}
