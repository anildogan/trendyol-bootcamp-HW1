package Model;

import Enums.Language;

import java.util.ArrayList;

public class CompanyDTO {

    private String companyName;
    private double money;
    private BasePackageSmsDTO smsPackage;
    private BasePackageEmailDTO emailPackage;

    private Language language;

    public CompanyDTO(String companyName, Language language) {
        this.companyName = companyName ;
        this.language = language;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public BasePackageSmsDTO getSmsPackage() {
        return smsPackage;
    }

    public void setSmsPackage(BasePackageSmsDTO smsPackage) {
        setMoney(this.getMoney() - smsPackage.getPrice());
        this.smsPackage = smsPackage;
    }

    public BasePackageEmailDTO getEmailPackage() {
        return emailPackage;
    }

    public void setEmailPackage(BasePackageEmailDTO emailPackage) {
        this.emailPackage = emailPackage;
    }
}
