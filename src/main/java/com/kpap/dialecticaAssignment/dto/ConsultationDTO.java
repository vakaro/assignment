package com.kpap.dialecticaAssignment.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ConsultationDTO implements Serializable {

    private static final long serialVersionUID = -6130355548844913233L;
    private Long id;
    @NotBlank
    private String title;
    private Date date;
    private String note;
    @Size(min = 1)
    private int duration;
    @Size(min = 0)
    private double payableAmount;
    private long userId;
    @NotNull
    private Long consultantId;
    private boolean badCall;
    @NotBlank
    private String status;

    public ConsultationDTO() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Long getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(Long consultantId) {
        this.consultantId = consultantId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(double payableAmount) {
        this.payableAmount = payableAmount;
    }

    public boolean isBadCall() {
        return badCall;
    }

    public void setBadCall(boolean badCall) {
        this.badCall = badCall;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ConsultationDTO that = (ConsultationDTO) o;
        return duration == that.duration && Double.compare(that.payableAmount, payableAmount) == 0
                && userId == that.userId && consultantId == that.consultantId && badCall == that.badCall
                && Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(date, that.date)
                && Objects.equals(note, that.note) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, date, note, duration, payableAmount, userId, consultantId, badCall, status);
    }
}
