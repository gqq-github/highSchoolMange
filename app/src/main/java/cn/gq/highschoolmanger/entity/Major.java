package cn.gq.highschoolmanger.entity;

public class Major {
    private Integer mid;
    private String  mname;
    private String  mdetail;
    private Integer collegeid;

    public Major(Integer mid, String mname) {
        this.mid = mid;
        this.mname = mname;
    }

    public Major() {

    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getMdetail() {
        return mdetail;
    }

    public void setMdetail(String mdetail) {
        this.mdetail = mdetail;
    }

    public Integer getCollegeid() {
        return collegeid;
    }

    public void setCollegeid(Integer collegeid) {
        this.collegeid = collegeid;
    }
}
