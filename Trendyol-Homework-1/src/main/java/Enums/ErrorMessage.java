package Enums;

public enum ErrorMessage {

    Sms_Error_Tr("Sms kotası aşıldı!"),
    Sms_Error_En("Sms validation limit exceeded!"),
    Email_Error_Tr("Email kotası aşıldı!"),
    Email_Error_En("Email validation limit exceeded!"),
    Email_Blacklist_Err_Tr("2 aylık ödeme süreniz doldu, karalisteye alındınız"),
    Email_Blacklist_Err_En("Your time of 2 months has exceeded, you have been blacklisted"),
    Money_Err_En("You dont have enough money"),
    Money_Err_Tr("Hesabınızda para yok");



    String message;
    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
