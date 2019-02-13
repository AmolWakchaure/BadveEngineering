package engineering.badve.badveengineering.adapter;

/**
 * Created by snsystem_amol on 28/06/2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import engineering.badve.badveengineering.R;
import engineering.badve.badveengineering.pojo.TypeInOutTargetInfo;


public class TypeInTargetOutAdapter extends RecyclerView.Adapter<TypeInTargetOutAdapter.ViewHolderCarLog> {

    private ArrayList<TypeInOutTargetInfo> carlogInformation = new ArrayList<>();
    private LayoutInflater layoutInflater;

    View view ;

    public TypeInTargetOutAdapter(Context context)
    {
        layoutInflater = LayoutInflater.from(context);

    }

    public void setBookingData(ArrayList<TypeInOutTargetInfo> countryInformationsdata) {
        this.carlogInformation = countryInformationsdata;
        notifyItemRangeChanged(0, countryInformationsdata.size());
    }

    @Override
    public TypeInTargetOutAdapter.ViewHolderCarLog onCreateViewHolder(ViewGroup parent, int viewType) {

        view = layoutInflater.inflate(R.layout.zxing_desig_in_ot_target, parent, false);
        TypeInTargetOutAdapter.ViewHolderCarLog viewHolderScheduleholde = new TypeInTargetOutAdapter.ViewHolderCarLog(view);
        return viewHolderScheduleholde;
    }

    @Override
    public void onBindViewHolder(TypeInTargetOutAdapter.ViewHolderCarLog holder, int position) {
        try
        {

            TypeInOutTargetInfo carLogInformation = carlogInformation.get(position);

            holder.designationTextView.setText(carLogInformation.getDesignationType());
            String inTarget = carLogInformation.getInTarget();
            String inCount = carLogInformation.getInCount();
            String outCount = carLogInformation.getOutCount();
            String shiftName = carLogInformation.getShiftName();
            holder.inTargetTextView.setText(inTarget);
            holder.inCountTextView.setText(inCount);
            holder.outCountTextView.setText(outCount);
            holder.shiftNameTextView.setText(shiftName);
            holder.dateOutTextView.setText("("+carLogInformation.getDateDate()+")");

            /*Integer inTargett = Integer.valueOf(inTarget);
            Integer inCountt = Integer.valueOf(inCount);
            Integer outCountt = Integer.valueOf(outCount);


            String outEntryStatus = carLogInformation.getOutEntryStatus();*/

           /* if(outEntryStatus.equals("old"))
            {
                holder.designationLinearLayout.setBackgroundColor(Color.parseColor("#E1BEE7"));
                holder.inTargetLinearLayout.setBackgroundColor(Color.parseColor("#E1BEE7"));
                holder.shiftLinearLayout.setBackgroundColor(Color.parseColor("#E1BEE7"));
            }*/

            /*if(inTargett == inCountt && !(inTargett == 0 && inCountt == 0))
            {
                holder.inCountLinearLayout.setBackgroundColor(Color.parseColor("#C8E6C9"));
            }
            if(inTargett == outCountt && !(inTargett == 0 && outCountt == 0))
            {
                holder.outCountLinearLayout.setBackgroundColor(Color.parseColor("#C8E6C9"));
            }
            if(inCountt == outCountt && !(inCountt == 0 && outCountt == 0))
            {
                holder.inTargetLinearLayout.setBackgroundColor(Color.parseColor("#F8BBD0"));
            }*/
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

    class ViewHolderCarLog extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView
                designationTextView,
                inTargetTextView,
                inCountTextView,
                shiftNameTextView,
                outCountTextView,
                dateOutTextView;

        private LinearLayout
                inCountLinearLayout,
                outCountLinearLayout,
                designationLinearLayout,
                inTargetLinearLayout,
                shiftLinearLayout;

        public ViewHolderCarLog(final View itemView)
        {
            super(itemView);

            try
            {
                designationTextView = (TextView) itemView.findViewById(R.id.designationTextView);
                inTargetTextView = (TextView) itemView.findViewById(R.id.inTargetTextView);
                inCountTextView = (TextView) itemView.findViewById(R.id.inCountTextView);
                outCountTextView = (TextView) itemView.findViewById(R.id.outCountTextView);
                shiftNameTextView = (TextView) itemView.findViewById(R.id.shiftNameTextView);
                dateOutTextView = (TextView) itemView.findViewById(R.id.dateOutTextView);

                inCountLinearLayout = (LinearLayout) itemView.findViewById(R.id.inCountLinearLayout);
                outCountLinearLayout = (LinearLayout) itemView.findViewById(R.id.outCountLinearLayout);
                designationLinearLayout = (LinearLayout) itemView.findViewById(R.id.designationLinearLayout);
                inTargetLinearLayout = (LinearLayout) itemView.findViewById(R.id.inTargetLinearLayout);
                shiftLinearLayout = (LinearLayout) itemView.findViewById(R.id.shiftLinearLayout);

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
            }

        }

    }

}
