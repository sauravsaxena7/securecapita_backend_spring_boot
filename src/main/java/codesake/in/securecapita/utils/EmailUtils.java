package codesake.in.securecapita.utils;

public class EmailUtils {

    public static String getEmailMessage(String name,String host,String token){
        return "Hello "+name+",\n\nYour new account has been created. please click the link below to verify your account. \n\n"
                +getVerificationUrl(host,token)
                +"\n\n CodeSake Support team.";
    }

    public static String getVerificationUrl(String host, String token) {
        return host+"/api/v1/user/confirmUserAccount?token="+token;
    }


}
