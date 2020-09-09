package com.company;

import Enums.Language;
import Model.CompanyDTO;
import Model.EmailFlexibleDTO;
import Model.SmsFlexibleDTO;
import Model.SmsStableDTO;
import Service.CompanyService;
import Service.DateService;
import Service.FlexiblePackageService;
import Service.StablePackageService;


public class Main {


    public static void main(String[] args) {
        FlexiblePackageService flexiblePackageService = new FlexiblePackageService();
        StablePackageService stablePackageService = new StablePackageService();
        CompanyService companyService = new CompanyService();


        CompanyDTO from = new CompanyDTO("1st company",Language.TR);
        CompanyDTO to = new CompanyDTO("2st company",Language.EN);

        from.setMoney(100);

        companyService.setSmsPackage(from, new SmsStableDTO()); //20 unit cost
        companyService.setEmailPackage(from, new EmailFlexibleDTO());//10 unit cost

        for(int i = 0 ; i < (from.getSmsPackage().getLimit() + 2); i ++) {
            stablePackageService.sendSms(from, to);
        }

        for(int i = 0 ; i < (from.getEmailPackage().getLimit() + 2); i ++) {
            flexiblePackageService.sendEmail(from, to);
        }

    }
}
