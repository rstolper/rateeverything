package com.romanstolper.rateeverything.user.external.dto;

public class NativeAuthCreateDTO {
    private String username;
    private String password;
    private boolean usernameIsEmail;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUsernameIsEmail() {
        return usernameIsEmail;
    }

    public void setUsernameIsEmail(boolean usernameIsEmail) {
        this.usernameIsEmail = usernameIsEmail;
    }
}
