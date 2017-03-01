package com.example.forex;


public class TrueFxResponse {

    public TrueFxResponse() {
    }

    private String td1;
    private String td2;
    private String td3;
    private String td4;
    private String td5;
    private String td6;
    private String td7;
    private String td8;
    private String td9;

    public TrueFxResponse(String td1, String td2, String td3, String td4, String td5, String td6, String td7, String td8, String td9) {
        this.td1 = td1;
        this.td2 = td2;
        this.td3 = td3;
        this.td4 = td4;
        this.td5 = td5;
        this.td6 = td6;
        this.td7 = td7;
        this.td8 = td8;
        this.td9 = td9;
    }

    public String getTd1() {
        return td1;
    }

    public void setTd1(String td1) {
        this.td1 = td1;
    }

    public String getTd2() {
        return td2;
    }

    public void setTd2(String td2) {
        this.td2 = td2;
    }

    public String getTd3() {
        return td3;
    }

    public void setTd3(String td3) {
        this.td3 = td3;
    }

    public String getTd4() {
        return td4;
    }

    public void setTd4(String td4) {
        this.td4 = td4;
    }

    public String getTd5() {
        return td5;
    }

    public void setTd5(String td5) {
        this.td5 = td5;
    }

    public String getTd6() {
        return td6;
    }

    public void setTd6(String td6) {
        this.td6 = td6;
    }

    public String getTd7() {
        return td7;
    }

    public void setTd7(String td7) {
        this.td7 = td7;
    }

    public String getTd8() {
        return td8;
    }

    public void setTd8(String td8) {
        this.td8 = td8;
    }

    public String getTd9() {
        return td9;
    }

    public void setTd9(String td9) {
        this.td9 = td9;
    }

    @Override
    public String toString() {
        return "TrueFxResponse{" +
                "td1='" + td1 + '\'' +
                ", td2='" + td2 + '\'' +
                ", td3='" + td3 + '\'' +
                ", td4='" + td4 + '\'' +
                ", td5='" + td5 + '\'' +
                ", td6='" + td6 + '\'' +
                ", td7='" + td7 + '\'' +
                ", td8='" + td8 + '\'' +
                ", td9='" + td9 + '\'' +
                '}';
    }
}
