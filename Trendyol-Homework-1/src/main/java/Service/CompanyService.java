package Service;

import Enums.ErrorMessage;
import Enums.Language;
import Exceptions.CannotPayException;
import Model.BasePackageEmailDTO;
import Model.BasePackageSmsDTO;
import Model.CompanyDTO;

public class CompanyService {

    public void setSmsPackage(CompanyDTO companyDTO, BasePackageSmsDTO smsPackageDTO) {
        try{
            pay(companyDTO, smsPackageDTO.getPrice());
            companyDTO.setSmsPackage(smsPackageDTO);
        }
        catch (CannotPayException e) {
            System.out.println(e);
        }
    }

    public void setEmailPackage(CompanyDTO companyDTO, BasePackageEmailDTO emailPackageDTO) {
        try{
            pay(companyDTO, emailPackageDTO.getPrice());
            companyDTO.setEmailPackage(emailPackageDTO);
        }
        catch (CannotPayException e) {
            System.out.println(e);
        }
    }

    public void pay(CompanyDTO companyDTO, double amount) throws CannotPayException {
        if(canPay(companyDTO,amount)) {
            companyDTO.setMoney(companyDTO.getMoney() - amount);
        }
        else {
            String errorMessage = companyDTO.getLanguage() == Language.EN ? ErrorMessage.Money_Err_En.getMessage() : ErrorMessage.Money_Err_Tr.getMessage();
            throw new CannotPayException(errorMessage);
        }
    }

    public boolean canPay(CompanyDTO companyDTO, double amount) {
        return (companyDTO.getMoney() - amount) >= 0 ;
    }
}
