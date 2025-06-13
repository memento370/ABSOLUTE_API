package com.example.absoluteweb.forum.DTO;

public class AvatarUploadRequest {
    private Long userId;
    private String base64Image;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}
