package com.sporrong.shoppinglist2;

import java.text.SimpleDateFormat;

public class Meeting {
    private String meetingDate;
    private String meetingInfo;
    private String pushKey;

    public Meeting() {
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public Meeting(String meetingDate, String meetingInfo) {

        this.meetingDate = meetingDate;
        this.meetingInfo = meetingInfo;
    }

    public String getMeetingDate() {

        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingInfo() {
        return meetingInfo;
    }

    public void setMeetingInfo(String meetingInfo) {
        this.meetingInfo = meetingInfo;
    }
}
