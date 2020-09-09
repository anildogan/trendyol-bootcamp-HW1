package Enums;

public enum ErrorMessage {
    Blacklist_Err_Tr("2 aylık ödeme süreniz doldu, karalisteye alındınız"),
    Blacklist_Err_En("Your time of 2 months has exceeded, you have been blacklisted"),
    Money_Err_En("You dont have enough money"),
    Money_Err_Tr("Hesabınızda para yok"),
    No_Limit_Tr("Hesabınıza tanımlı bir paket bulunamamıştır"),
    No_Limit_En("You dont have any package");

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
