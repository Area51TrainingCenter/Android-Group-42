package pe.area51.githublist.domain;

import android.support.annotation.NonNull;

public class User {

    @NonNull
    private final String userName;

    public User(@NonNull String userName) {
        this.userName = userName;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userName.equals(user.userName);
    }

    @Override
    public int hashCode() {
        return userName.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
