package hu.bme.museum.model;

public class User {
    public String email;
    public String name;
    public long lastActive;
    public String imageLink;
    public int score;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLastActive(long lastActive) {
        this.lastActive = lastActive;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public long getLastActive() {
        return lastActive;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getEmail() {
        return email;
    }

    public int getScore() {
        return score;
    }

    public String getName() {

        return name;
    }
}
