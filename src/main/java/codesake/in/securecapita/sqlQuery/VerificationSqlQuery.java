package codesake.in.securecapita.sqlQuery;

public class VerificationSqlQuery {
    public static final String COUNT_VERIFICATION_TOKEN_EMAIL_USER_ACCOUNT_QUERY
            = "SELECT COUNT(*) FROM accountverification WHERE username= :email and type= :type";

    public static final String UPDATE_ACCOUNT_VERIFICATION_TOKEN_EMAIL_AND_TYPE=
            "UPDATE accountverification SET token= :token WHERE username= :email and type= :type";

    public static final String INSERT_ACCOUNT_VERIFICATION_TOKEN=
            "INSERT INTO accountverification (user_id,token,username,type) VALUES (:userId, :token, :email, :type)";

    public static final String SELECT_USER_ID_BY_TOKEN_EMAIL_AND_TYPE
            = "SELECT user_id FROM accountverification WHERE username= :email and type= :type and token= :token";


    public static final String UPDATE_USER_ENABLE_MODE_BY_USER_ID =
            "UPDATE Users SET enabled= :flag where id= :userId";

    public static final String SELECT_ENABLE_FROM_USER_BY_USER_ID
            = "SELECT enabled FROM users WHERE id= :userId";


}
