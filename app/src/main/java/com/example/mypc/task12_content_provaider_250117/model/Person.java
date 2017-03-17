package com.example.mypc.task12_content_provaider_250117.model;

import java.io.Serializable;

/**
 * Created by MyPc on 14.12.2016.
 */

public class Person implements Serializable {

    private int mid;
    private String mName;
    private String mSurname;
    private String mPhoneNumber;
    private String mMail;
    private String mSkype;
    private String mProfile;

    public static Person selectPerson;

    public Person() {
        super();
    }

    public Person(int mid, String mName, String mSurname, String mPhoneNumber, String mMail, String mSkype, String mProfile) {
        this.mid = mid;
        this.mName = mName;
        this.mSurname = mSurname;
        this.mPhoneNumber = mPhoneNumber;
        this.mMail = mMail;
        this.mSkype = mSkype;
        this.mProfile = mProfile;
    }

    public Person(int mid, String mName, String mSurname, String mPhoneNumber, String mMail, String mSkype) {
        this.mid = mid;
        this.mName = mName;
        this.mSurname = mSurname;
        this.mPhoneNumber = mPhoneNumber;
        this.mMail = mMail;
        this.mSkype = mSkype;
    }

    public Person(String mName, String mSurname, String mPhoneNumber, String mMail, String mSkype) {

        this.mName = mName;
        this.mSurname = mSurname;
        this.mPhoneNumber = mPhoneNumber;
        this.mMail = mMail;
        this.mSkype = mSkype;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmSurname(String mSurname) {
        this.mSurname = mSurname;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public void setmMail(String mMail) {
        this.mMail = mMail;
    }

    public void setmSkype(String mSkype) {
        this.mSkype = mSkype;
    }

    public int getMid() {

        return mid;
    }
    public String getmProfile() {
        return mProfile;
    }

    public void setmProfile(String mProfile) {
        this.mProfile = mProfile;
    }

    public String getmName() {
        return mName;
    }

    public String getmSurname() {
        return mSurname;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public String getmMail() {
        return mMail;
    }

    public String getmSkype() {
        return mSkype;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj)
//            return true;
//        Person p = (Person) obj;
//        return mid == p.mid;
//    }

    //нет хеш кода
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mid;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (mid != other.mid)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Person{" +
                "mid=" + mid +
                ", mName='" + mName + '\'' +
                ", mSurname='" + mSurname + '\'' +
                ", mPhoneNumber='" + mPhoneNumber + '\'' +
                ", mMail='" + mMail + '\'' +
                ", mSkype='" + mSkype + '\'' +
                '}';
    }
}
