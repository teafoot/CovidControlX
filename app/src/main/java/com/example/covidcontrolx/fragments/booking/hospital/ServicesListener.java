package com.example.covidcontrolx.fragments.booking.hospital;

import java.util.List;

public interface ServicesListener {
    public void onServicesChanged(List<String> services);
    public void expandGroupEvent(int groupPosition, boolean isExpanded);
}
