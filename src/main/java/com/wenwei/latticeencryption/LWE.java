package com.wenwei.latticeencryption;

import com.wenwei.latticeencryption.Components.Parameters;
import com.wenwei.latticeencryption.Utils.Matrix;

public class LWE {
    Parameters params;

    Matrix PK1;
    Matrix error;
    Matrix SK;//s
    Matrix PK2;//stA+e
    Matrix message;//m
    Matrix r;//r

    Matrix C1;
    Matrix C2;

    public LWE() throws Exception {
        params=new Parameters();
        PK1 = new Matrix(params.n, params.m, params.q, params.q);//A
        error = new Matrix(params.m, 1, params.q, params.sigma);//e
        SK = new Matrix(params.n, 1, params.q, params.q);//s
        PK2 = SK.transpose().mul(PK1).add(error.transpose());//stA+e
        r = new Matrix(params.m, 1, params.q, 2);//r
    }
    public LWE(Parameters params) throws Exception {
        this.params=params;
        PK1 = new Matrix(params.n, params.m, params.q, params.q);//A
        error = new Matrix(params.m, 1, params.q, params.sigma);//e
        SK = new Matrix(params.n, 1, params.q, params.q);//s
        PK2 = SK.transpose().mul(PK1).add(error.transpose());//stA+e
        r = new Matrix(params.m, 1, params.q, 2);//r
    }
    public void setMessage(boolean m){
        if(m) {
            message = new Matrix(1L, params.q);
        }else {
            message = new Matrix(0L, params.q);
        }
        message.scale(params.q / 2 + 1);//m*=q/2+1
    }
    public void Encrypt() throws Exception {
        C1 = PK1.mul(r);
        C2 = PK2.mul(r).add(message);
    }
    public Matrix[] getCiphertext(){
        return new Matrix[]{C1,C2};
    }

    public Parameters getParams() {
        return params;
    }

    public Matrix getSK() {
        return SK;
    }

    public Matrix getC1() {
        return C1;
    }

    public Matrix getC2() {
        return C2;
    }

    @Override
    public String toString() {
        return params.toString() +
                "\nC1:\n" + C1.toString() +
                "\nC2:\n" + C2.toString();
    }

    public void setParams(Parameters params) {
        this.params = params;
    }

    public void setSK(Matrix SK) {
        this.SK = SK;
    }

    public void setC1(Matrix c1) {
        C1 = c1;
    }

    public void setC2(Matrix c2) {
        C2 = c2;
    }

    public boolean Decrypt() throws Exception {
        if (Math.abs(C2.sub(SK.transpose().mul(C1)).value()) < params.q / 4) {
            return false;
        }else {
            return true;
        }
    }
}
