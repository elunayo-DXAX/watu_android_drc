package com.watudrc.app.objects;

import com.google.gson.annotations.SerializedName;

public class AdCard {
    @SerializedName("type")
    private String type;

    @SerializedName("link_en")
    private String linkEn;

    @SerializedName("link_sw")
    private String linkSw;

    @SerializedName("click_open_tab")
    private String clickOpenTab;

    public String getType() { return type; }
    public String getLinkEn() { return linkEn; }
    public String getLinkSw() { return linkSw; }
    public String getClickOpenTab() { return clickOpenTab; }
}