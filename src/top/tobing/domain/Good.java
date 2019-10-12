package top.tobing.domain;

import java.util.Date;

public class Good {
    private int gid;
    private String goodname;
    private String goodbrand;
    private Date intime;
    private Date saletime;
    private double inprice;
    private double saleprice;
    private int stock;
    private String agent;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getGoodname() {
        return goodname;
    }

    public void setGoodname(String goodname) {
        this.goodname = goodname;
    }

    public String getGoodbrand() {
        return goodbrand;
    }

    public void setGoodbrand(String goodbrand) {
        this.goodbrand = goodbrand;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Date getSaletime() {
        return saletime;
    }

    public void setSaletime(Date saletime) {
        this.saletime = saletime;
    }

    public double getInprice() {
        return inprice;
    }

    public void setInprice(double inprice) {
        this.inprice = inprice;
    }

    public double getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(double saleprice) {
        this.saleprice = saleprice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    @Override
    public String toString() {
        return "Good{" +
                "gid=" + gid +
                ", goodname='" + goodname + '\'' +
                ", goodbrand='" + goodbrand + '\'' +
                ", intime=" + intime +
                ", saletime=" + saletime +
                ", inprice=" + inprice +
                ", saleprice=" + saleprice +
                ", stock=" + stock +
                ", agent='" + agent + '\'' +
                '}';
    }
}
