package com.wyb.jpa.mock;

import lombok.Getter;

public class CurrUserContext {

    public static User getCurrUser() {
        return new User("111", "xiaoming");
    }

    @Getter
    public static class User {
        private final String userId;
        private final String name;

        public User(String userId, String name) {
            this.userId = userId;
            this.name = name;
        }
    }
}
