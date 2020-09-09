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

    boolean isEmailLimitExceeded(CompanyDTO companyDTO);
    boolean isSmsLimitExceeded(CompanyDTO companyDTO);

    void sendEmailWithExceededPackage(CompanyDTO companyDTO) throws CannotPayException;
    void sendSmsWithExceededPackage(CompanyDTO companyDTO) throws CannotPayException;

    void isEmailExists(CompanyDTO companyDTO) throws NoPackageException;
    void isSmsExists(CompanyDTO companyDTO) throws NoPackageException;







}
