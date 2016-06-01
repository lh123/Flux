package com.lh.flux.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class WelfareInfoEntity {
    private String returnCode;
    private String msg;
    private Data data;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return "000".equals(returnCode);
    }

    public boolean isGrabing() {
        return "grabing".equals(data.getFlag());
    }

    public static class Data {
        private String flag;
        private int remain;
        private String startTime;
        private Rule rule;

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public int getRemain() {
            return remain;
        }

        public void setRemain(int remain) {
            this.remain = remain;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public Rule getRule() {
            return rule;
        }

        public void setRule(Rule rule) {
            this.rule = rule;
        }
    }

    public static class Rule {
        @SerializedName("_id")
        private String id;
        private int period;
        private int sustain;
        private int totalNum;
        private int status;
        @SerializedName("__v")
        private int v;
        private ArrayList<Prize> prize;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public int getSustain() {
            return sustain;
        }

        public void setSustain(int sustain) {
            this.sustain = sustain;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getV() {
            return v;
        }

        public void setV(int v) {
            this.v = v;
        }

        public ArrayList<Prize> getPrize() {
            return prize;
        }

        public void setPrize(ArrayList<Prize> prize) {
            this.prize = prize;
        }
    }

    public static class Prize {
        private int number;
        @SerializedName("_id")
        private String id;
        @SerializedName("id")
        private WelfareEnvelopEntity data;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public WelfareEnvelopEntity getData() {
            return data;
        }

        public void setData(WelfareEnvelopEntity data) {
            this.data = data;
        }
    }
}
