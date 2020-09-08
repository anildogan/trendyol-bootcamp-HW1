package Model;

import java.util.Date;

public abstract class BasePackageSmsDTO {

    private int limit;
    private double price;
    private double exceedPrice;
    private int currentLimit = 0 ;
    private Date lastPayment;
    private boolean isBlacklisted;

    public BasePackageSmsDTO(int limit, double price, double exceedPrice,
                             int currentLimit, Date lastPayment, boolean isBlacklisted) {
        this.limit = limit;
        this.price = price;
        this.exceedPrice = exceedPrice;
        this.currentLimit = currentLimit;
        this.lastPayment = lastPayment;
        this.isBlacklisted = isBlacklisted;
    }

    public boolean isBlacklisted() {
        return isBlacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        isBlacklisted = blacklisted;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getExceedPrice() {
        return exceedPrice;
    }

    public void setExceedPrice(double exceedPrice) {
        this.exceedPrice = exceedPrice;
    }

    public int getCurrentLimit() {
        return currentLimit;
    }

    public void setCurrentLimit(int currentLimit) {
        this.currentLimit = currentLimit;
    }

    public Date getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(Date lastPayment) {
        this.lastPayment = lastPayment;
    }
}
