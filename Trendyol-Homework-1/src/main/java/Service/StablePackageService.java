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
            isEmailExists(from.getEmailPackage());
            checkEmailBlacklisted(from);
            if(isEmailLimitExceeded(from)){
                refreshPackageEmail(from);
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
            isSmsExists(from.getSmsPackage());
            checkSmsBlacklisted(from);
            if(isSmsLimitExceeded(from)) {
                refreshPackageSms(from);
            }
            else {
                System.out.println("Sms sent successfully");
                from.getSmsPackage().setCurrentLimit(from.getSmsPackage().getCurrentLimit() - 1);
                System.out.println("New limit: " + from.getSmsPackage().getCurrentLimit());
            }
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
    public void refreshPackageEmail(CompanyDTO from) throws CannotPayException {
        double newAmount = from.getMoney() - from.getEmailPackage().getExceedPrice();
        if(newAmount < 0) {
            String errorMessage = from.getLanguage() == Language.EN ? ErrorMessage.Money_Err_En.getMessage() : ErrorMessage.Money_Err_Tr.getMessage();
            throw new CannotPayException(errorMessage);
        }
        from.setMoney( newAmount);
        from.getEmailPackage().setCurrentLimit(from.getEmailPackage().getLimit());
    }

    @Override
    public void refreshPackageSms(CompanyDTO from) throws CannotPayException {
        double newAmount = from.getMoney() - from.getSmsPackage().getExceedPrice();
        if(newAmount < 0) {
            String errorMessage = from.getLanguage() ==Language.EN ? ErrorMessage.Money_Err_En.getMessage() : ErrorMessage.Money_Err_Tr.getMessage();
            throw new CannotPayException(errorMessage);
        }
        from.setMoney( newAmount);
        from.getSmsPackage().setCurrentLimit(from.getSmsPackage().getLimit());
    }

    @Override
    public void checkEmailBlacklisted(CompanyDTO companyDTO) throws BlacklistException {
        if(isEmailBlacklistTimeExceeded(companyDTO) || companyDTO.getEmailPackage().isBlacklisted()){
            setEmailBlacklisted(companyDTO);
            String errorMessage = companyDTO.getLanguage() == Language.EN ? ErrorMessage.Email_Blacklist_Err_En.getMessage():ErrorMessage.Email_Blacklist_Err_Tr.getMessage();
            throw new BlacklistException(errorMessage);
        }
    }

    @Override
    public void checkSmsBlacklisted(CompanyDTO companyDTO) throws BlacklistException {
        if(isSmsBlacklistTimeExceeded(companyDTO) || companyDTO.getSmsPackage().isBlacklisted()){
            setSmsBlacklisted(companyDTO);
            String errorMessage = companyDTO.getLanguage() == Language.EN ? ErrorMessage.Email_Blacklist_Err_En.getMessage():ErrorMessage.Email_Blacklist_Err_Tr.getMessage();
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
    public void isEmailExists(BasePackageEmailDTO basePackageEmailDTO) throws NoPackageException {
        if(basePackageEmailDTO == null) {
            throw new NoPackageException("No new package");
        }
    }

    @Override
    public void isSmsExists(BasePackageSmsDTO basePackageSmsDTO) throws NoPackageException {
        if(basePackageSmsDTO == null) {
            throw new NoPackageException("No new package");
        }
    }
}
