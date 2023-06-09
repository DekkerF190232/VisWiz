package de.sirvierl0ffel.viswiz.models;

import java.time.LocalDateTime;

public class Post {

    private User user;
    private LocalDateTime dateTime;

    public Post() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Post(User user, Algorithm algorithm, LocalDateTime dateTime) {
        this.user = user;
        this.dateTime = dateTime;
    }
}
