package com.example.dellpc.start;

/**
 * Created by dell pc on 14-Feb-17.
 */

public class User {

        private String id;
        private String name;
        private String email;
        private String photoUrl;
        private String key;

        public User()
        {}

        public User(String id, String name, String email,String photoUrl,String key) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.photoUrl = photoUrl;
            this.key = key;
        }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
