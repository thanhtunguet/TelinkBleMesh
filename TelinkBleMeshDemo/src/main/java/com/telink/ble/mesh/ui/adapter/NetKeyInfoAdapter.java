/********************************************************************************************************
 * @file SchedulerListAdapter.java
 *
 * @brief for TLSR chips
 *
 * @author telink
 * @date Sep. 30, 2010
 *
 * @par Copyright (c) 2010, Telink Semiconductor (Shanghai) Co., Ltd.
 *           All rights reserved.
 *
 *			 The information contained herein is confidential and proprietary property of Telink 
 * 		     Semiconductor (Shanghai) Co., Ltd. and is available under the terms 
 *			 of Commercial License Agreement between Telink Semiconductor (Shanghai) 
 *			 Co., Ltd. and the licensee in separate contract or the terms described here-in. 
 *           This heading MUST NOT be removed from this file.
 *
 * 			 Licensees are granted free, non-transferable use of the information in this 
 *			 file under Mutual Non-Disclosure Agreement. NO WARRENTY of ANY KIND is provided. 
 *
 *******************************************************************************************************/
package com.telink.ble.mesh.ui.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.telink.ble.mesh.demo.R;
import com.telink.ble.mesh.model.MeshNetKey;
import com.telink.ble.mesh.util.Arrays;

import java.util.List;

public class NetKeyInfoAdapter extends BaseRecyclerViewAdapter<NetKeyInfoAdapter.ViewHolder> {

    private final List<MeshNetKey> keyList;
    private final Context mContext;
    private final View.OnLongClickListener keyLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            int position = (int) v.getTag();
            String keyStr = Arrays.bytesToHexString(keyList.get(position).getKey());
            if (copyTextToClipboard(keyStr)) {
                Toast.makeText(mContext, "key copied", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    };

    public NetKeyInfoAdapter(Context context, List<MeshNetKey> keyList) {
        mContext = context;
        this.keyList = keyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_net_key_info, parent, false);
        ViewHolder holder = new ViewHolder(itemView);

        holder.tv_key_name = itemView.findViewById(R.id.tv_key_name);
        holder.tv_key_index = itemView.findViewById(R.id.tv_key_index);
        holder.tv_key_val = itemView.findViewById(R.id.tv_key_val);
        holder.ll_key_val = itemView.findViewById(R.id.ll_key_val);
        return holder;
    }

    @Override
    public int getItemCount() {
        return keyList == null ? 0 : keyList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        MeshNetKey netKey = keyList.get(position);
        holder.tv_key_name.setText(netKey.getName());
        holder.tv_key_index.setText(String.format("0x%02X", netKey.getIndex()));
        holder.tv_key_val.setText(Arrays.bytesToHexString(netKey.getKey()));
        holder.ll_key_val.setTag(position);
        holder.ll_key_val.setOnLongClickListener(keyLongClick);
    }

    private boolean copyTextToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("com.telink.bluetooth.light", text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            return true;
        } else {
            return false;
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {


        private TextView tv_key_name, tv_key_index, tv_key_val;
        private View ll_key_val;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
