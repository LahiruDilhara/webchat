package me.lahirudilhara.webchat.dto.room;

public class UpdateRoomDTO {
    private Boolean isPrivate;
    private String name;
    private Boolean multiUser;

    public UpdateRoomDTO(Boolean isPrivate, String name, Boolean multiUser) {
        this.isPrivate = isPrivate;
        this.name = name;
        this.multiUser = multiUser;
    }

    public UpdateRoomDTO() {
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getMultiUser() {
        return multiUser;
    }

    public void setMultiUser(Boolean multiUser) {
        this.multiUser = multiUser;
    }
}
