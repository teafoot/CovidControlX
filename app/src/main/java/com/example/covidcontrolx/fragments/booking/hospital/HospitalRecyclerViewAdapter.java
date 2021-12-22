package com.example.covidcontrolx.fragments.booking.hospital;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.covidcontrolx.R;
import com.example.covidcontrolx.fragments.booking.models.Hospital;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HospitalRecyclerViewAdapter extends RecyclerView.Adapter<HospitalRecyclerViewAdapter.ViewHolder> {
    Context context;
    private final List<Hospital> hospitalList;
    private final OnHospitalClickListener onHospitalClickListener;

    public interface OnHospitalClickListener {
        void onHospitalClicked(Hospital hospital);
    }

    public HospitalRecyclerViewAdapter(Context context, List<Hospital> hospitalList, OnHospitalClickListener onHospitalClickListener) {
        this.context = context;
        this.hospitalList = hospitalList;
        this.onHospitalClickListener = onHospitalClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hospital hospital = hospitalList.get(position);

        Glide.with(context).load(hospital.getImage()).override(1400, 280).into(holder.hospitalImage);
        holder.hospitalName.setText(hospital.getName());
        holder.hospitalAddress.setText(hospital.getLocation().toString());
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener { // each row item
        public ImageView hospitalImage;
        public TextView hospitalName;
        public TextView hospitalAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            hospitalImage = itemView.findViewById(R.id.hospital_image);
            hospitalName = itemView.findViewById(R.id.txt_hospital_name);
            hospitalAddress = itemView.findViewById(R.id.txt_hospital_address);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Hospital hospital = hospitalList.get(getAdapterPosition());
            onHospitalClickListener.onHospitalClicked(hospital); // HospitalFragment
        }
    }
}
