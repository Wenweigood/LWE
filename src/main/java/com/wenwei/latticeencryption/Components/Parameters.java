package com.wenwei.latticeencryption.Components;

public class Parameters {
    public long q=999983L;
    public int n=128;
    public int m=128;
    public double sigma=2.8;
    public Parameters(){}
    public Parameters(int n, int m, long q, double sigma){
        this.n=n;
        this.m=m;
        this.q=q;
        this.sigma=sigma;
    }

    public void setQ(long q) {
        this.q = q;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    @Override
    public String toString() {
        return "n:"+n+"\n"+"m:"+m+"\n"+
                "q:"+q+"\n"+"sigma:"+sigma+"\n";
    }
}
