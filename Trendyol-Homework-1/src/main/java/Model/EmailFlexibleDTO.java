package Model;

import java.util.Date;

public class EmailFlexibleDTO extends BasePackageEmailDTO {
/*
    public BasePackageEmailDTO(int limit, double price, double exceedPrice, int currentLimit, Date lastPayment) {
        this.limit = limit;
        this.price = price;
        this.exceedPrice = exceedPrice;
        this.currentLimit = currentLimit;
        this.lastPayment = lastPayment;
    }*/

    public EmailFlexibleDTO() {
        super(2000, 7.5, 0.03, 2000, new Date(), false);
    }
}
