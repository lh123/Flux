package com.lh.flux.model.entity;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class WelfareEnvelopEntity {
    @SerializedName("__v")
    private int v;
    @SerializedName("_id")
    private String id;
    private String description;
    private boolean enabled;
    private String itemId;
    private String logoUrl;
    private String name;
    private int prizeValue;
    private String slogan;
    private String type;

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrizeValue() {
        return prizeValue;
    }

    public void setPrizeValue(int prizeValue) {
        this.prizeValue = prizeValue;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
