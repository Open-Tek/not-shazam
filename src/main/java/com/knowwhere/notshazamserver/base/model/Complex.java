package com.knowwhere.notshazamserver.base.model;

public class Complex {
    private double real;
    private double imaginary;

    public Complex(double real, double imaginary){
        this.real = real;
        this.imaginary = imaginary;
    }


    public Complex add(Complex complex){
        this.real += complex.real;
        this.imaginary += complex.imaginary;
        return this;

    }

    public Complex sub(Complex complex){
        this.real -= complex.real;
        this.imaginary -=complex.imaginary;
        return this;

    }

    public Complex mul(Complex complex){
        this.real = this.real * complex.real - this.imaginary * complex.imaginary;
        this.imaginary = this.real * complex.imaginary + this.imaginary * complex.real;
        return this;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public double abs(){
        return Math.hypot(this.real, this.imaginary);
    }

    /**
     * This method adds 2 static complex numbers and stores the result in the first instance provided
     * @param complex1: Complex instance 1
     * @param operation: The operation to be performed (+-*)
     * @param complex2: Complex instance 2
     *                NOTE - An unsupported operation exception is thrown if invalid operations are submitted to this method
     */
    public static void performExp(Complex complex1, char operation,Complex complex2){
        switch (operation){
            case '+':
                complex1.add(complex2);
                break;

            case '-':
                complex1.sub(complex2);
                break;

            case '*':
                complex1.mul(complex2);
                break;
                default:
                    throw new UnsupportedOperationException("operation "+operation+" is not allowed ");
        }
    }




}
