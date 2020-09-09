package Test;
import Enums.Language;
import Exceptions.BlacklistException;
import Exceptions.CannotPayException;
import Exceptions.NoPackageException;
import Model.*;
import Service.CompanyService;
import Service.DateService;
import Service.FlexiblePackageService;
import Service.StablePackageService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PackageTest {
    @Test
    public void it_should_decrease_limit_when_message_with_stable_package_sent() {
        //Given
        StablePackageService stablePackageService = new StablePackageService();
        CompanyService companyService = new CompanyService();


        CompanyDTO from = new CompanyDTO("1st company", Language.TR);
        CompanyDTO to = new CompanyDTO("2st company",Language.EN);

        from.setMoney(100);

        companyService.setSmsPackage(from, new SmsStableDTO());
        companyService.setEmailPackage(from, new EmailStableDTO());


        //When
        stablePackageService.sendSms(from, to);
        stablePackageService.sendEmail(from, to);

        //Then
        assertEquals((from.getEmailPackage().getCurrentLimit()), (from.getEmailPackage().getLimit() -1));
        assertEquals((from.getSmsPackage().getCurrentLimit()), (from.getSmsPackage().getLimit() -1));
    }

    @Test
    public void it_should_decrease_limit_when_message_with_flexible_package_sent() {
        //Given
        FlexiblePackageService flexiblePackageService = new FlexiblePackageService();
        CompanyService companyService = new CompanyService();


        CompanyDTO from = new CompanyDTO("1st company", Language.TR);
        CompanyDTO to = new CompanyDTO("2st company",Language.EN);

        from.setMoney(100);

        companyService.setSmsPackage(from, new SmsFlexibleDTO());
        companyService.setEmailPackage(from, new EmailFlexibleDTO());


        //When
        flexiblePackageService.sendSms(from, to);
        flexiblePackageService.sendEmail(from, to);

        //Then
        assertEquals((from.getEmailPackage().getCurrentLimit()), (from.getEmailPackage().getLimit() -1));
        assertEquals((from.getSmsPackage().getCurrentLimit()), (from.getSmsPackage().getLimit() -1));
    }

    @Test
    public void it_should_draw_money_on_stable_package_when_limit_is_zero() {
        //Given
        //Given
        StablePackageService stablePackageService = new StablePackageService();
        CompanyService companyService = new CompanyService();


        CompanyDTO from = new CompanyDTO("1st company", Language.TR);
        CompanyDTO to = new CompanyDTO("2st company",Language.EN);

        from.setMoney(100);

        companyService.setSmsPackage(from, new SmsStableDTO());//20 unit cost
        companyService.setEmailPackage(from, new EmailStableDTO());//10 unit cost

        from.getSmsPackage().setCurrentLimit(0);
        from.getEmailPackage().setCurrentLimit(0);

        //When
        stablePackageService.sendSms(from, to);
        stablePackageService.sendEmail(from, to);

        //Then
        assertEquals(from.getMoney(), 40);
    }

    @Test
    public void it_should_draw_money_on_flexible_message_on_every_messages_when_limit_is_zero() {
        //Given
        FlexiblePackageService flexiblePackageService = new FlexiblePackageService();
        CompanyService companyService = new CompanyService();


        CompanyDTO from = new CompanyDTO("1st company", Language.TR);
        CompanyDTO to = new CompanyDTO("2st company",Language.EN);

        from.setMoney(100);

        companyService.setSmsPackage(from, new SmsFlexibleDTO());//30 unit cost
        companyService.setEmailPackage(from, new EmailFlexibleDTO());//7.5 unit cost

        from.getSmsPackage().setCurrentLimit(0);
        from.getEmailPackage().setCurrentLimit(0);

        //When
        flexiblePackageService.sendSms(from, to);//0.1 on every message
        flexiblePackageService.sendEmail(from, to);//0.03 on every message

        //Then
        assertEquals(from.getMoney(), (100 - 30 - 7.5 - 0.1 - 0.03));
    }
    @Test
    public void it_should_give_exception_when_company_blacklisted_and_language_is_turkish() {
        //Given
        StablePackageService stablePackageService = new StablePackageService();
        CompanyService companyService = new CompanyService();
        DateService dateService = new DateService();

        CompanyDTO from = new CompanyDTO("1st company", Language.TR);
        CompanyDTO to = new CompanyDTO("2st company",Language.EN);

        from.setMoney(100);
        //When
        companyService.setSmsPackage(from, new SmsStableDTO());
        companyService.setEmailPackage(from, new EmailStableDTO());
        from.getSmsPackage().setLastPayment(dateService.setDate(2020,2,10));
        from.getEmailPackage().setLastPayment(dateService.setDate(2020,2,10));


        //Then
        Exception exceptionOnSms = assertThrows(BlacklistException.class, () -> stablePackageService.checkSmsBlacklisted(from));
        Exception exceptionOnEmail = assertThrows(BlacklistException.class, () -> stablePackageService.checkEmailBlacklisted(from));


        assertEquals(exceptionOnSms.getMessage(), "2 aylık ödeme süreniz doldu, karalisteye alındınız");
        assertEquals(exceptionOnEmail.getMessage(), "2 aylık ödeme süreniz doldu, karalisteye alındınız");

    }
    @Test
    public void it_should_give_exception_when_company_blacklisted_and_language_is_english() {
        //Given
        StablePackageService stablePackageService = new StablePackageService();
        CompanyService companyService = new CompanyService();
        DateService dateService = new DateService();

        CompanyDTO from = new CompanyDTO("1st company", Language.EN);
        CompanyDTO to = new CompanyDTO("2st company",Language.EN);

        from.setMoney(100);
        //When
        companyService.setSmsPackage(from, new SmsStableDTO());
        companyService.setEmailPackage(from, new EmailStableDTO());
        from.getSmsPackage().setLastPayment(dateService.setDate(2020,2,10));
        from.getEmailPackage().setLastPayment(dateService.setDate(2020,2,10));


        //Then
        Exception exceptionOnSms = assertThrows(BlacklistException.class, () -> stablePackageService.checkSmsBlacklisted(from));
        Exception exceptionOnEmail = assertThrows(BlacklistException.class, () -> stablePackageService.checkEmailBlacklisted(from));


        assertEquals(exceptionOnSms.getMessage(), "Your time of 2 months has exceeded, you have been blacklisted");
        assertEquals(exceptionOnEmail.getMessage(), "Your time of 2 months has exceeded, you have been blacklisted");
    }

    @Test
    public void it_should_throw_cannot_pay_exception_when_insufficent_funds_and_language_is_turkish() {
        //Given
        StablePackageService stablePackageService = new StablePackageService();
        CompanyService companyService = new CompanyService();

        CompanyDTO from = new CompanyDTO("1st company", Language.TR);
        CompanyDTO to = new CompanyDTO("2st company",Language.EN);

        from.setMoney(0);

        //When
        //Then
        Exception exceptionOnSms = assertThrows(CannotPayException.class, () -> companyService.pay(from, (new SmsStableDTO()).getPrice()));
        Exception exceptionOnEmail = assertThrows(CannotPayException.class, () -> companyService.pay(from, (new SmsStableDTO()).getPrice()));

        assertEquals(exceptionOnSms.getMessage(), "Hesabınızda para yok");
        assertEquals(exceptionOnEmail.getMessage(), "Hesabınızda para yok");
    }
    @Test
    public void it_should_throw_cannot_pay_exception_when_insufficent_funds_and_language_is_english() {
        //Given
        StablePackageService stablePackageService = new StablePackageService();
        CompanyService companyService = new CompanyService();

        CompanyDTO from = new CompanyDTO("1st company", Language.EN);
        CompanyDTO to = new CompanyDTO("2st company",Language.EN);

        from.setMoney(0);

        //When
        //Then
        Exception exceptionOnSms = assertThrows(CannotPayException.class, () -> companyService.pay(from, (new SmsStableDTO()).getPrice()));
        Exception exceptionOnEmail = assertThrows(CannotPayException.class, () -> companyService.pay(from, (new SmsStableDTO()).getPrice()));

        assertEquals(exceptionOnSms.getMessage(), "You dont have enough money");
        assertEquals(exceptionOnEmail.getMessage(), "You dont have enough money");
    }

    @Test
    public void it_should_throw_no_package_exception_when_there_are_no_package_when_language_is_turkish() {
        //Given
        StablePackageService stablePackageService = new StablePackageService();

        CompanyDTO from = new CompanyDTO("1st company", Language.TR);
        CompanyDTO to = new CompanyDTO("2st company",Language.TR);

        //When
        /*There is no package*/

        //Then
        Exception exceptionOnSms = assertThrows(NoPackageException.class, () -> stablePackageService.isSmsExists(from));
        Exception exceptionOnEmail = assertThrows(NoPackageException.class, () -> stablePackageService.isEmailExists(from));

        assertEquals(exceptionOnSms.getMessage(), "Hesabınıza tanımlı bir paket bulunamamıştır");
        assertEquals(exceptionOnEmail.getMessage(), "Hesabınıza tanımlı bir paket bulunamamıştır");
    }

    @Test
    public void it_should_throw_no_package_exception_when_there_are_no_package_when_language_is_english() {
        //Given
        FlexiblePackageService flexiblePackageService = new FlexiblePackageService();

        CompanyDTO from = new CompanyDTO("1st company", Language.EN);
        CompanyDTO to = new CompanyDTO("2st company",Language.TR);

        //When
        /*There is no package*/

        //Then
        Exception exceptionOnSms = assertThrows(NoPackageException.class, () -> flexiblePackageService.isSmsExists(from));
        Exception exceptionOnEmail = assertThrows(NoPackageException.class, () -> flexiblePackageService.isEmailExists(from));

        assertEquals(exceptionOnSms.getMessage(), "You dont have any package");
        assertEquals(exceptionOnEmail.getMessage(), "You dont have any package");
    }

    @Test
    public void it_should_refresh_limit_when_limit_is_zero_and_there_are_enough_money_with_stable_package() {
        //Given
        StablePackageService stablePackageService = new StablePackageService();
        CompanyService companyService = new CompanyService();

        CompanyDTO from = new CompanyDTO("1st company", Language.TR);
        CompanyDTO to = new CompanyDTO("2st company",Language.TR);

        //When
        from.setMoney(200);
        companyService.setSmsPackage(from, new SmsStableDTO());//10 unit cost
        companyService.setEmailPackage(from, new EmailStableDTO());//20 unit cost

        from.getEmailPackage().setCurrentLimit(0);
        from.getSmsPackage().setCurrentLimit(0);

        //Then
        stablePackageService.sendSms(from, to);
        stablePackageService.sendEmail(from, to);

        assertEquals(from.getMoney(), (200 - 20 - 10 - 20 - 10));

        assertEquals(from.getSmsPackage().getCurrentLimit(), (from.getSmsPackage().getLimit() - 1 ));
        assertEquals(from.getEmailPackage().getCurrentLimit(), (from.getEmailPackage().getLimit() - 1 ));
    }
    
}
