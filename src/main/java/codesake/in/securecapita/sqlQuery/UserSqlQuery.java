package codesake.in.securecapita.sqlQuery;

public class UserSqlQuery {
    public static final String COUNT_USER_EMAIL_QUERY = "SELECT COUNT(*) FROM Users WHERE email= :email";
    public static final String INSERT_USER_QUERY = "INSERT INTO Users (first_name,last_name,email,password,enabled) VALUES (:firstName, :lastName, :email, :password,:enabled)";
    public static final String INSERT_VERIFICATION_QUERY = "INSERT INTO AccountVerification (user_id,url) VALUES (:userId, :url)";

    public static final String FIND_USER_BY_EMAIL = "SELECT * FROM Users WHERE email= :email";
}
