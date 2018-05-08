package com.arifinfrds.papblprojectakhir.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arifinfrds.papblprojectakhir.R;
import com.arifinfrds.papblprojectakhir.model.Toko;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.arifinfrds.papblprojectakhir.util.Constant.TAG.TAG_ADMIN;

/**
 * Created by Galang on 5/7/2018.
 */

public class TokoListAdapter extends RecyclerView.Adapter<TokoListAdapter.TokoViewHolder> {

    Context context;
    List<Toko> tokos;

    public TokoListAdapter(Context context, List<Toko> tokos) {
        this.context = context;
        this.tokos = tokos;
    }

    @Override
    public TokoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.toko_row, parent, false);
        return new TokoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TokoViewHolder holder, int position) {
        holder.mTvTelp.setText("Call: " + tokos.get(position).getNomorTelepon());
        holder.mTvKet.setText(tokos.get(position).getKeterangan());
        holder.mTvNama.setText(tokos.get(position).getNama());

        Picasso.get().load(tokos.get(position).getPhotoUrl()).into(holder.mImgV);

    }

    @Override
    public int getItemCount() {
        Log.d(TAG_ADMIN, "onDataChange: getItemCount: " + tokos.size());
        return tokos.size();
    }

    class TokoViewHolder extends RecyclerView.ViewHolder {

        TextView mTvNama, mTvKet, mTvTelp;
        ImageView mImgV;


        public TokoViewHolder(View itemView) {
            super(itemView);
            mTvNama = itemView.findViewById(R.id.tv_TokoListNama);
            mTvKet = itemView.findViewById(R.id.tv_TokoListKeterengan);
            mTvTelp = itemView.findViewById(R.id.tv_NoTelpToko);

            mImgV = itemView.findViewById(R.id.imgView_Toko);
        }
    }
}
