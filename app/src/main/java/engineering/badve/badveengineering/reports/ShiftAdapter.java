package engineering.badve.badveengineering.reports;

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

/**
 * Created by Amol on 28-February-18.
 */

/*public class ShiftAdapter
{

}*/
public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ViewHolderCarLog> {

    private ArrayList<ShiftInfo> carlogInformation = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private ShiftActivity activity;

    View view ;

    public ShiftAdapter(Context context)
    {
        layoutInflater = LayoutInflater.from(context);

    }

    public void setCellId(ArrayList<ShiftInfo> countryInformationsdata,ShiftActivity activity) {
        this.carlogInformation = countryInformationsdata;
        this.activity = activity;
        notifyItemRangeChanged(0, countryInformationsdata.size());
    }

    @Override
    public ShiftAdapter.ViewHolderCarLog onCreateViewHolder(ViewGroup parent, int viewType) {

        view = layoutInflater.inflate(R.layout.shift_row, parent, false);
        ShiftAdapter.ViewHolderCarLog viewHolderScheduleholde = new ShiftAdapter.ViewHolderCarLog(view);
        return viewHolderScheduleholde;
    }

    @Override
    public void onBindViewHolder(ShiftAdapter.ViewHolderCarLog holder, int position) {
        try
        {



            ShiftInfo carLogInformation = carlogInformation.get(position);


            holder.idTextView.setText(carLogInformation.getId());
            holder.nameTxt.setText(carLogInformation.getName());
            holder.fromTime.setText(carLogInformation.getFromTime());
            holder.totime.setText(carLogInformation.getToTime());
            holder.date.setText(carLogInformation.getDate());
            holder.sstatus.setText(carLogInformation.getName());


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
                nameTxt,
                fromTime,
                totime,
                date,
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
                nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
                fromTime = (TextView) itemView.findViewById(R.id.fromTime);
                totime = (TextView) itemView.findViewById(R.id.totime);
                date = (TextView) itemView.findViewById(R.id.date);
                sstatus = (TextView) itemView.findViewById(R.id.sstatus);



                itemView.setOnClickListener(this);

                updateButton.setOnClickListener(this);

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {


                        ShiftInfo bookingDetailsInformation = carlogInformation.get(Integer.valueOf(getAdapterPosition()));
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
                    ShiftInfo bookingDetailsInformation = carlogInformation.get(Integer.valueOf(getAdapterPosition()));
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
