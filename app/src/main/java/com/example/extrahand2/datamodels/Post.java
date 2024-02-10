package com.example.extrahand2.datamodels;

import com.google.firebase.Timestamp;

public class Post {
        private String description, owner, type, location, picUrl;
        private Timestamp timeAdded;

        public Post(){} //Empty creator
        public Post(String description, String owner, String type, com.google.firebase.Timestamp timeAdded, String location, String picUrl){
                this.description = description;
                this.owner = owner;
                this.type = type;
                this.timeAdded = timeAdded;
                this.location = location;
                this.picUrl = picUrl;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public String getOwner() {
                return owner;
        }

        public void setOwner(String owner) {
                this.owner = owner;
        }

        public String getType() {
                return type;
        }

        public void setType(String type) {
                this.type = type;
        }

        public String getLocation() {
                return location;
        }

        public void setLocation(String location) {
                this.location = location;
        }

        public Timestamp getTimeAdded() {
                return timeAdded;
        }

        public void setTimeAdded(Timestamp timeAdded) {
                this.timeAdded = timeAdded;
        }

        public String getPicUrl() {
                return picUrl;
        }

        public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
        }
}
