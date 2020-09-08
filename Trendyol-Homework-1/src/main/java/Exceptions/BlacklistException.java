package Exceptions;

import Model.CompanyDTO;

public class BlacklistException extends Exception {

    public BlacklistException(String message) {
        super(message);
    }

}
