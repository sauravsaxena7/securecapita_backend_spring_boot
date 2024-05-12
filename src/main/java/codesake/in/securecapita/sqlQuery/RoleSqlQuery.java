package codesake.in.securecapita.sqlQuery;

public class RoleSqlQuery {
    public static final String INSERT_ROLE_TO_USER_QUERY = "INSERT INTO UserRoles (user_id,role_id) VALUES (:userId, :roleId)";
    public static final String SELECT_ROLE_BY_NAME_QUERY="SELECT * FROM Roles WHERE name= :name";
    public static final String GET_ALL_ROLES_BY_USER_ID=
             "SELECT ur.user_id, ur.role_id,r.name,r.permissions "
             + "FROM UserRoles ur "
             +"INNER JOIN Users u on u.id=ur.user_id "
             +"INNER JOIN Roles r on r.id=ur.role_id "
             +"WHERE ur.user_id=:userId";




}
