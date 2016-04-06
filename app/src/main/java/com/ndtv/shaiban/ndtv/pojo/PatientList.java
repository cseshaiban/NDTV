package com.ndtv.shaiban.ndtv.pojo;

/**
 * Created by shaiban on 5/4/16.
 */
public class PatientList {

    public String name, gender, bloodGroup, city, address, emailId, profile, mobile, date;
    public int id, age;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}


//"name":"Rishav",
//        "id":"2",
//        "age":"28",
//        "gender":"Male",
//        "bloodGroup":"A+",
//        "city":"Bangalore",
//        "address":"#24, 1st Main, Vijay Nagar Bangalore",
//        "emailId":"rishav@gmail.com",
//        "profile":"http://thejakartaglobe.beritasatu.com/img/523515-640x360.jpg",
//        "mobileNo":"9922338957",
//        "date":"2016-04-30T04:20:30+01:00"