package Service;

import Enums.ErrorMessage;
import Enums.Language;
import Exceptions.BlacklistException;
import Exceptions.CannotPayException;
import Exceptions.NoPackageException;
import Model.BasePackageEmailDTO;
import Model.BasePackageSmsDTO;
import Model.CompanyDTO;

import java.util.Date;

public class FlexiblePackageService implements BaseFlexiblePackageService{
    private DateService dateService = new DateService();
    @Override
    public void sendEmail(CompanyDTO from, CompanyDTO to) {
        //Check if black listed and if 2 months exceeded blacklist
        try {
            isEmailExists(from);
            checkEmailBlacklisted(from);
            if(isEmailLimitExceeded(from)){
                sendEmailWithExceededPackage(from);
            }
            else {
                System.out.println("Email sent successfully");
                from.getEmailPackage().setCurrentLimit(from.getEmailPackage().getCurrentLimit() - 1);
                System.out.println("New limit: " + from.getEmailPackage().getCurrentLimit());
            }
        }catch (BlacklistException | CannotPayException | NoPackageException  e) {
            System.out.println(e);
        }

    }

    @Override
    public void sendSms(CompanyDTO from, CompanyDTO to) {
        try{
            isSmsExists(from);
            checkSmsBlacklisted(from);
            if(isSmsLimitExceeded(from)) {
                sendSmsWithExceededPackage(from);
            }
            else {
                System.out.println("Sms sent successfully");
                from.getSmsPackage().setCurrentLimit(from.getSmsPackage().getCurrentLimit() - 1);
                System.out.println("New limit: " + from.getSmsPackage().getCurrentLimit());
            }
        }catch (BlacklistException | CannotPayException | NoPackageException  e) {
            System.out.println(e);
        }
    }

    @Override
    public boolean isEmailBlacklistTimeExceeded(CompanyDTO companyDTO) {
        return isTimeExceeded(companyDTO.getEmailPackage().getLastPayment());
    }

    @Override
    public boolean isSmsBlacklistTimeExceeded(CompanyDTO companyDTO) {
        return isTimeExceeded(companyDTO.getSmsPackage().getLastPayment());

    }

    public boolean isTimeExceeded(Date lastPayment) {
        return this.dateService.betweenDates(lastPayment, (new Date())) > 60 ;
    }

    @Override
    public void setEmailBlacklisted(CompanyDTO companyDTO) {
        companyDTO.getEmailPackage().setBlacklisted(true);
    }

    @Override
    public void setSmsBlacklisted(CompanyDTO companyDTO) {
        companyDTO.getSmsPackage().setBlacklisted(true);
    }

    @Override
    public void checkEmailBlacklisted(CompanyDTO companyDTO) throws BlacklistException {
        if(isEmailBlacklistTimeExceeded(companyDTO) || companyDTO.getEmailPackage().isBlacklisted()){
            setEmailBlacklisted(companyDTO);
            String errorMessage = companyDTO.getLanguage() == Language.EN ? ErrorMessage.Blacklist_Err_En.getMessage():ErrorMessage.Blacklist_Err_Tr.getMessage();
            throw new BlacklistException(errorMessage);
        }
    }

    @Override
    public void checkSmsBlacklisted(CompanyDTO companyDTO) throws BlacklistException {
        if(isSmsBlacklistTimeExceeded(companyDTO) || companyDTO.getSmsPackage().isBlacklisted()){
            setSmsBlacklisted(companyDTO);
            String errorMessage = companyDTO.getLanguage() == Language.EN ? ErrorMessage.Blacklist_Err_En.getMessage():ErrorMessage.Blacklist_Err_Tr.getMessage();
            throw new BlacklistException(errorMessage);
        }
    }

    @Override
    public boolean isEmailLimitExceeded(CompanyDTO from) {
        return from.getEmailPackage().getCurrentLimit() <= 0 ;
    }

    @Override
    public boolean isSmsLimitExceeded(CompanyDTO from) {
        return from.getSmsPackage().getCurrentLimit() <= 0 ;
    }

    @Override
    public void sendEmailWithExceededPackage(CompanyDTO from) throws CannotPayException {

        System.out.println("Sending with exceeded account");
        double newAmount = from.getMoney() - from.getEmailPackage().getExceedPrice();
        if(newAmount < 0) {
            String errorMessage = from.getLanguage() ==Language.EN ? ErrorMessage.Money_Err_En.getMessage() : ErrorMessage.Money_Err_Tr.getMessage();
            throw new CannotPayException(errorMessage);
        }
        from.setMoney( from.getMoney() - from.getEmailPackage().getExceedPrice());
        System.out.println("New balance: " + from.getMoney());
    }

    @Override
    public void sendSmsWithExceededPackage(CompanyDTO from) throws CannotPayException {
        System.out.println("Sending with exceeded account");
        double newAmount = from.getMoney() - from.getSmsPackage().getExceedPrice();
        if(newAmount < 0) {
            String errorMessage = from.getLanguage() ==Language.EN ? ErrorMessage.Money_Err_En.getMessage() : ErrorMessage.Money_Err_Tr.getMessage();
            throw new CannotPayException(errorMessage);
        }
        from.setMoney( from.getMoney() - from.getSmsPackage().getExceedPrice());
        System.out.println("New balance: " + from.getMoney());
    }
    @Override
    public void isEmailExists(CompanyDTO companyDTO) throws NoPackageException {
        if(companyDTO.getEmailPackage() == null) {
            String errorMessage = companyDTO.getLanguage() == Language.EN ? ErrorMessage.No_Limit_En.getMessage():ErrorMessage.No_Limit_Tr.getMessage();
            throw new NoPackageException(errorMessage);
        }
    }

    @Override
    public void isSmsExists(CompanyDTO companyDTO) throws NoPackageException {
        if(companyDTO.getSmsPackage() == null) {
            String errorMessage = companyDTO.getLanguage() == Language.EN ? ErrorMessage.No_Limit_En.getMessage():ErrorMessage.No_Limit_Tr.getMessage();
            throw new NoPackageException(errorMessage);
        }
    }
}
