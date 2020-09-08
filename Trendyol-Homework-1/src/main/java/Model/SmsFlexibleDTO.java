package Model;

import java.util.Date;

public class SmsFlexibleDTO extends BasePackageSmsDTO {
    public SmsFlexibleDTO() {
        super(3000, 30, 0.1, 3000, new Date(), false );
    }
}
//    public BasePackageSmsDTO(int limit, double price, double exceedPrice, int currentLimit, Date lastPayment) {