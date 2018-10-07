package com.sporrong.shoppinglist2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

public class MeetingViewHolder extends ViewHolder {
    private TextView meetingPointInfo;
    private TextView meetingPointDate;

    public MeetingViewHolder(@NonNull View itemView) {
        super(itemView);
        meetingPointDate = itemView.findViewById(R.id.meetingPointDate);
        meetingPointInfo = itemView.findViewById(R.id.meetingPointInfo);
    }

    public void bind(Meeting meeting) {
        meetingPointInfo.setText(meeting.getMeetingInfo());
        meetingPointDate.setText(meeting.getMeetingDate());
    }
}
