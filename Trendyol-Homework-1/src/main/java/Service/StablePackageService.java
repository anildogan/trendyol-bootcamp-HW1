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

public class StablePackageService implements BaseStablePackageService{
    private DateService dateService = new DateService();
    @Override
    public void sendEmail(CompanyDTO from, CompanyDTO to) {
        //Check if black listed and if 2 months exceeded blacklist
        try {
            isEmailExists(from);
            checkEmailBlacklisted(from);
            if(isEmailLimitExceeded(from)){
                refreshPackageEmail(from);
            }
                System.out.println("Email sent successfully");
                from.getEmailPackage().setCurrentLimit(from.getEmailPackage().getCurrentLimit() - 1);
                System.out.println("New limit: " + from.getEmailPackage().getCurrentLimit());
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
                refreshPackageSms(from);
            }
                System.out.println("Sms sent successfully");
                from.getSmsPackage().setCurrentLimit(from.getSmsPackage().getCurrentLimit() - 1);
                System.out.println("New limit: " + from.getSmsPackage().getCurrentLimit());

        }catch (BlacklistException | CannotPayException | NoPackageException e) {
            System.out.println(e);
        }
    }

    public boolean isSmsBlacklistTimeExceeded(CompanyDTO companyDTO) {
        return isTimeExceeded(companyDTO.getSmsPackage().getLastPayment());

    }
    @Override
    public boolean isEmailBlacklistTimeExceeded(CompanyDTO companyDTO) {
        return isTimeExceeded(companyDTO.getEmailPackage().getLastPayment());
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
    public void refreshPackageEmail(CompanyDTO companyDTO) throws CannotPayException {
        double newAmount = companyDTO.getMoney() - companyDTO.getEmailPackage().getExceedPrice();
        if(newAmount < 0) {
            String errorMessage = companyDTO.getLanguage() == Language.EN ? ErrorMessage.Money_Err_En.getMessage() : ErrorMessage.Money_Err_Tr.getMessage();
            throw new CannotPayException(errorMessage);
        }
        companyDTO.setMoney( newAmount);
        companyDTO.getEmailPackage().setCurrentLimit(companyDTO.getEmailPackage().getLimit());
        System.out.println("Email package redefined");
        System.out.println("New balance: " + companyDTO.getMoney());
    }

    @Override
    public void refreshPackageSms(CompanyDTO companyDTO) throws CannotPayException {
        double newAmount = companyDTO.getMoney() - companyDTO.getSmsPackage().getExceedPrice();
        if(newAmount < 0) {
            String errorMessage = companyDTO.getLanguage() ==Language.EN ? ErrorMessage.Money_Err_En.getMessage() : ErrorMessage.Money_Err_Tr.getMessage();
            throw new CannotPayException(errorMessage);
        }
        companyDTO.setMoney( newAmount);
        companyDTO.getSmsPackage().setCurrentLimit(companyDTO.getSmsPackage().getLimit());
        System.out.println("Sms package redefined");
        System.out.println("New balance: " + companyDTO.getMoney());

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
    public boolean isEmailLimitExceeded(CompanyDTO companyDTO) {
        return companyDTO.getEmailPackage().getCurrentLimit() <= 0 ;
    }

    @Override
    public boolean isSmsLimitExceeded(CompanyDTO companyDTO) {
        return companyDTO.getSmsPackage().getCurrentLimit() <= 0 ;
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
