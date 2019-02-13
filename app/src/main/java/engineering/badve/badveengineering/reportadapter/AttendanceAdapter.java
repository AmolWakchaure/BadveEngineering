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
import engineering.badve.badveengineering.pojo.BookingDetailsInformation;
import engineering.badve.badveengineering.reports.AttendadanceInfo;
import engineering.badve.badveengineering.reports.AttendanceActivity;
import engineering.badve.badveengineering.reports.TableBookingDetailsActivity;

/**
 * Created by Amol on 21-February-18.
 */


public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolderCarLog> {

    private ArrayList<AttendadanceInfo> carlogInformation = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private AttendanceActivity activity;

    View view ;

    public AttendanceAdapter(Context context)
    {
        layoutInflater = LayoutInflater.from(context);

    }

    public void setCellId(ArrayList<AttendadanceInfo> countryInformationsdata,AttendanceActivity activity) {
        this.carlogInformation = countryInformationsdata;
        this.activity = activity;
        notifyItemRangeChanged(0, countryInformationsdata.size());
    }

    @Override
    public ViewHolderCarLog onCreateViewHolder(ViewGroup parent, int viewType) {

        view = layoutInflater.inflate(R.layout.attendance_row, parent, false);
        ViewHolderCarLog viewHolderScheduleholde = new ViewHolderCarLog(view);
        return viewHolderScheduleholde;
    }

    @Override
    public void onBindViewHolder(ViewHolderCarLog holder, int position) {
        try
        {



            AttendadanceInfo carLogInformation = carlogInformation.get(position);


            holder.idTextView.setText(carLogInformation.getId());
            holder.rfidNumber.setText(carLogInformation.getRfidNumber());
            holder.bnumber.setText(carLogInformation.getBnumber());
            holder.intime.setText(carLogInformation.getIntime());
            holder.outime.setText(carLogInformation.getOuttime());
            holder.name.setText(carLogInformation.getName());
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
                intime,
                outime,
                name,
                sstatus;
        private Button

                updateButton;

        private LinearLayout
                removeCellId;


        public ViewHolderCarLog(final View itemView)
        {
            super(itemView);

            try
            {

                updateButton = (Button) itemView.findViewById(R.id.updateButton);

                idTextView = (TextView) itemView.findViewById(R.id.idTextView);
                rfidNumber = (TextView) itemView.findViewById(R.id.rfidNumber);
                bnumber = (TextView) itemView.findViewById(R.id.bnumber);
                intime = (TextView) itemView.findViewById(R.id.intime);
                outime = (TextView) itemView.findViewById(R.id.outime);
                name = (TextView) itemView.findViewById(R.id.name);
                sstatus = (TextView) itemView.findViewById(R.id.sstatus);



                itemView.setOnClickListener(this);

                updateButton.setOnClickListener(this);

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {


                        AttendadanceInfo bookingDetailsInformation = carlogInformation.get(Integer.valueOf(getAdapterPosition()));
                        activity.deleteDataId(bookingDetailsInformation.getId());

                        return false;
                    }
                });


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


                if (v.getId() == updateButton.getId())
                {
                    AttendadanceInfo bookingDetailsInformation = carlogInformation.get(Integer.valueOf(getAdapterPosition()));
                    activity.updateDataId(bookingDetailsInformation.getId());
                }




            }
            catch (Exception e)
            {
                e.printStackTrace();
                T.t(v.getContext(),""+e);
            }

        }

    }



}