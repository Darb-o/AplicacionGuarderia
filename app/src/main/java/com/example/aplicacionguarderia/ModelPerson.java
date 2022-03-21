package com.example.aplicacionguarderia;

import java.io.Serializable;

public class ModelPerson implements Serializable {
    private String name,mail,pass,dni,adress,phone;

    public ModelPerson(String name, String mail, String pass, String dni, String adress, String phone) {
        this.name = name;
        this.mail = mail;
        this.pass = pass;
        this.dni = dni;
        this.adress = adress;
        this.phone = phone;
    }

    public ModelPerson(String name, String dni){
        this.name = name;
        this.dni = dni;
    }

    public boolean check(String data){
        if(data.isEmpty()){
            return true;
        }
        return false;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
