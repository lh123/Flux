package com.lh.flux.model.entity;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class FluxEntity {
    private String returnCode;
    private Data data;
    private String msg;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return "000".equals(returnCode);
    }

    @SuppressWarnings("unused")
    public static class Data {
        private Sum sum;
        private String updatedTime;
        private ArrayList<Packages> packages;

        public Sum getSum() {
            return sum;
        }

        public void setSum(Sum sum) {
            this.sum = sum;
        }

        public String getUpdatedTime() {
            return updatedTime;
        }

        public void setUpdatedTime(String updatedTime) {
            this.updatedTime = updatedTime;
        }

        public ArrayList<Packages> getPackages() {
            return packages;
        }

        public void setPackages(ArrayList<Packages> packages) {
            this.packages = packages;
        }
    }

    @SuppressWarnings("unused")
    public static class Sum {
        private int available;
        private int total;

        public int getAvailable() {
            return available;
        }

        public void setAvailable(int available) {
            this.available = available;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

    @SuppressWarnings("unused")
    public static class Packages {
        private String name;
        private int available;
        private int total;
        private int trans;
        private int unitTypeId;
        private String unitType;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAvailable() {
            return available;
        }

        public void setAvailable(int available) {
            this.available = available;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTrans() {
            return trans;
        }

        public void setTrans(int trans) {
            this.trans = trans;
        }

        public int getUnitTypeId() {
            return unitTypeId;
        }

        public void setUnitTypeId(int unitTypeId) {
            this.unitTypeId = unitTypeId;
        }

        public String getUnitType() {
            return unitType;
        }

        public void setUnitType(String unitType) {
            this.unitType = unitType;
        }
    }
}
