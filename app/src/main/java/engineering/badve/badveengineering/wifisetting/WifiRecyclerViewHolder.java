package engineering.badve.badveengineering.wifisetting;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import engineering.badve.badveengineering.R;

/**
 * Created by SAI on 5/17/2017.
 */

public class WifiRecyclerViewHolder extends RecyclerView.ViewHolder{

    private Context context;
    private TextView txtWifiName;
    private ScanResult scanResult;

    public WifiRecyclerViewHolder(View itemView, final WifiConfigurationActivity.OnListItemClick onListItemClick){
        super(itemView);
        context = itemView.getContext();

        txtWifiName = (TextView) itemView.findViewById(R.id.txt_wifi_name);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClick.onItemClick(scanResult);
            }
        });
    }

    public void setData(ScanResult scanResult){
        this.scanResult = scanResult;

        txtWifiName.setText(scanResult.SSID);
    }


}
