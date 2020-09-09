package Service;

import Exceptions.BlacklistException;
import Exceptions.CannotPayException;
import Exceptions.NoPackageException;
import Model.BasePackageEmailDTO;
import Model.BasePackageSmsDTO;
import Model.CompanyDTO;

public interface BaseStablePackageService {

    void sendEmail(CompanyDTO from, CompanyDTO to);
    void sendSms(CompanyDTO from, CompanyDTO to);

    boolean isEmailBlacklistTimeExceeded(CompanyDTO companyDTO);
    boolean isSmsBlacklistTimeExceeded(CompanyDTO companyDTO);

    void setEmailBlacklisted(CompanyDTO companyDTO);
    void setSmsBlacklisted(CompanyDTO companyDTO);

    void refreshPackageEmail(CompanyDTO from) throws CannotPayException;
    void refreshPackageSms(CompanyDTO from) throws CannotPayException;

    void checkEmailBlacklisted(CompanyDTO companyDTO) throws BlacklistException;
    void checkSmsBlacklisted(CompanyDTO companyDTO) throws BlacklistException;

    boolean isEmailLimitExceeded(CompanyDTO from);
    boolean isSmsLimitExceeded(CompanyDTO from);

    void isEmailExists(CompanyDTO companyDTO) throws NoPackageException;
    void isSmsExists(CompanyDTO companyDTO) throws NoPackageException;



/*
if(from.getEmailPackage().getCurrentLimit() <= 0) {
            if(from.getMoney() >= from.getEmailPackage().getPrice()) {
                from.setMoney(from.getMoney() - from.getEmailPackage().getPrice());
                from.getEmailPackage().setLimit(from.getEmailPackage().getLimit());
                System.out.println("New package has identified to your account");
            }
            String errorMessage = from.getLanguage() ==Language.EN ? ErrorMessage.Email_Error_En.getMessage() : ErrorMessage.Email_Error_Tr.getMessage();
            throw CannotPayException(errorMessage)
        }
 */
}
