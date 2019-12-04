package com.citic.conference.entity;

public class MeetingRoom {
    private Integer id;

    private String floor;

    private String roomNumber;

    private Integer seatNumber;

    private Integer principalId;

    private Integer multimedia;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Integer getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(Integer principalId) {
        this.principalId = principalId;
    }

    public Integer getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(Integer multimedia) {
        this.multimedia = multimedia;
    }
}
