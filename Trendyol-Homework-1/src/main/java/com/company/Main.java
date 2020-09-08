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
        DateService dateService = new DateService();
        FlexiblePackageService flexiblePackageService = new FlexiblePackageService();
        StablePackageService stablePackageService = new StablePackageService();
        CompanyService companyService = new CompanyService();


        CompanyDTO from = new CompanyDTO("1st company",Language.TR);
        CompanyDTO to = new CompanyDTO("2st company",Language.EN);

        from.setMoney(100);

        companyService.setSmsPackage(from, new SmsStableDTO());
        companyService.setEmailPackage(from, new EmailFlexibleDTO());

        System.out.println(from.getMoney());


        for(int i = 0 ; i < 2; i ++) {
            stablePackageService.sendSms(from, to);
        }

        for(int i = 0 ; i < 5; i ++) {
            flexiblePackageService.sendEmail(from, to);
        }

        //FlexiblePackageService flexiblePackageService = new FlexiblePackageService();
        //System.out.println(flexiblePackageService.isEmailBlacklisted(company1));
        /*
        *
        * BasePackageDTO dto = new SmsFixedDTO();


        BasePackageDTO[] base = new BasePackageDTO[2];

        base[0] = new SmsFixedDTO();
        base[1] = new EmailFixedDTO();

        base[0].saySa();
        base[1].saySa();
        *
        * */



    }
}
