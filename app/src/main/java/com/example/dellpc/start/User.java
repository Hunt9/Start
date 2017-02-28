package com.example.dellpc.start;

/**
 * Created by dell pc on 14-Feb-17.
 */

public class User {

        private String email;
        private String firstname;
        private String lastname;
        private String contactNo;
        private String photoUrl;

        public User()
        {}

        public User(String email, String firstname, String lastname,String contactNo,String photoUrl) {

            this.email = email;
            this.firstname = firstname;
            this.lastname = lastname;
            this.contactNo = contactNo;
            this.photoUrl = photoUrl;

        }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
