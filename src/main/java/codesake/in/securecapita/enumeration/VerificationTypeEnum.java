package codesake.in.securecapita.enumeration;

import java.util.Locale;

public enum VerificationTypeEnum {
    ACCOUNT("ACCOUNT"),
    PASSWORD("PASSWORD");

    private final String type;

    VerificationTypeEnum(String type){this.type=type;}

    public String getType(){
        return this.type.toLowerCase();
    }
}
