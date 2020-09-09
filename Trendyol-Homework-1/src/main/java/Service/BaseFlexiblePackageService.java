package Service;

import Exceptions.BlacklistException;
import Exceptions.CannotPayException;
import Exceptions.NoPackageException;
import Model.BasePackageEmailDTO;
import Model.BasePackageSmsDTO;
import Model.CompanyDTO;

public interface BaseFlexiblePackageService {

    void sendEmail(CompanyDTO from, CompanyDTO to);
    void sendSms(CompanyDTO from, CompanyDTO to);

    boolean isEmailBlacklistTimeExceeded(CompanyDTO companyDTO);
    boolean isSmsBlacklistTimeExceeded(CompanyDTO companyDTO);

    void setEmailBlacklisted(CompanyDTO companyDTO);
    void setSmsBlacklisted(CompanyDTO companyDTO);

    void checkEmailBlacklisted(CompanyDTO companyDTO) throws BlacklistException;
    void checkSmsBlacklisted(CompanyDTO companyDTO) throws BlacklistException;

    boolean isEmailLimitExceeded(CompanyDTO from);
    boolean isSmsLimitExceeded(CompanyDTO from);

    void sendEmailWithExceededPackage(CompanyDTO from) throws CannotPayException;
    void sendSmsWithExceededPackage(CompanyDTO from) throws CannotPayException;

    void isEmailExists(CompanyDTO companyDTO) throws NoPackageException;
    void isSmsExists(CompanyDTO companyDTO) throws NoPackageException;







}
