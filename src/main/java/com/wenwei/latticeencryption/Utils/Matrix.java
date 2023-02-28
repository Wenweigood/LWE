package com.wenwei.latticeencryption.Utils;

import java.util.Arrays;
import java.util.Random;

public class Matrix {
    long[][] data;
    long modulus;
    public Matrix (int rows, int cols, long modulus){
        data=new long[rows][cols];
        this.modulus=modulus;
    }
    public Matrix (long bit, long modulus){
        data=new long[][]{{bit}};
        this.modulus=modulus;
    }
    public Matrix(long[][] data, long modulus){
        this.data=data;
        this.modulus=modulus;
    }
    public Matrix(int rows,int cols, long modulus, long range){
        this.modulus=modulus;
        Random r=new Random();
        data=new long[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = r.nextLong(range);
            }
        }
    }
    public Matrix(int rows, int cols, long modulus, double sigma){
        this.modulus=modulus;
        data=new long[rows][cols];
        Random r=new Random();
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                data[i][j]=(long)(sigma*r.nextGaussian());
            }
        }
    }
    public int row(){return data.length;}
    public int col(){return data[0].length;}

    public void set(int x, int y, long value){
        data[x][y]=value;
    }
    public long get(int x, int y){return data[x][y];}

    public Matrix mul(Matrix B) throws Exception {
        if(this.col()!=B.row())
            throw new Exception("matrix dimension mismatch!");
        Matrix matrix=new Matrix(this.row(),B.col(),modulus);
        for(int i=0;i<this.row();i++){
            for(int j=0;j<B.col();j++){
                for(int k=0;k<B.row();k++){
                    matrix.set(i,j,(matrix.get(i,j)+(this.get(i,k)*B.get(k,j))%modulus)%modulus);
                }
            }
        }
        return matrix;
    }
    public Matrix add(Matrix B) throws Exception {
        if(this.row()!=B.row()||this.col()!=B.col())
            throw new Exception("matrix dimension mismatch!");
        Matrix matrix=new Matrix(this.row(),this.col(),modulus);
        for(int i=0;i<this.row();i++){
            for(int j=0;j<this.col();j++){
                matrix.set(i,j,(this.get(i,j)+B.get(i,j))%modulus);
            }
        }
        return matrix;
    }
    public Matrix sub(Matrix B) throws Exception {
        if(this.row()!=B.row()||this.col()!=B.col())
            throw new Exception("matrix dimension mismatch!");
        Matrix matrix=new Matrix(this.row(),this.col(),modulus);
        for(int i=0;i<this.row();i++){
            for(int j=0;j<this.col();j++){
                matrix.set(i,j,(this.get(i,j)-B.get(i,j))%modulus);
            }
        }
        return matrix;
    }
    public Matrix transpose(){
        Matrix matrix=new Matrix(this.col(),this.row(),modulus);
        for(int i=0;i<this.row();i++){
            for(int j=0;j<this.col();j++){
                matrix.set(j,i,data[i][j]);
            }
        }
        return matrix;
    }
    public void scale(long factor){
        for(int i=0;i<data.length;i++){
            for(int j=0;j<data[0].length;j++)
                data[i][j]=(data[i][j]*factor)%modulus;
        }
    }
    public long value() throws Exception {
        if(data.length==1&&data[0].length==1) {
            return data[0][0];
        }else {
            throw new Exception("Not a value");
        }
    }

    @Override
    public String toString() {
        String res="";
        for(int i=0;i<data.length;i++){
            res+=Arrays.toString(data[i])+"\n";
        }
        return res;
    }

    public void print(){
        for(int i=0;i<data.length;i++){
            for(int j=0;j<data[0].length;j++)
                System.out.print(data[i][j]+" ");
            System.out.println("");
        }
        System.out.println("");
    }
}
