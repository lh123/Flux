package com.lh.flux.model.entity;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class WelfareRecordEntity {
    private String returnCode;
    private ArrayList<Data> data;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return "000".equals(returnCode);
    }

    public static class Data {
        private String phone;
        private String envelopes;
        private int status;
        private String addtime;
        private WelfareEnvelopEntity prize;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEnvelopes() {
            return envelopes;
        }

        public void setEnvelopes(String envelopes) {
            this.envelopes = envelopes;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public WelfareEnvelopEntity getPrize() {
            return prize;
        }

        public void setPrize(WelfareEnvelopEntity prize) {
            this.prize = prize;
        }
    }
}
