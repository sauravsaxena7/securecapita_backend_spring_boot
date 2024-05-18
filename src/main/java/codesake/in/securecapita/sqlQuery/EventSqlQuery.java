package codesake.in.securecapita.sqlQuery;

public class EventSqlQuery {

    public static final String INSERT_EVENT_TO_USER_QUERY = "INSERT INTO userevents (user_id,event_id,device,ip_address) VALUES (:userId, :eventId,:device,:ip_address)";
    public static final String SELECT_EVENT_BY_NAME_QUERY="SELECT * FROM Events WHERE type= :type";
    public static final String GET_ALL_EVENTS_BY_USER_ID=
            "SELECT ue.id,ue.user_id, ue.event_id,e.type,e.description,ue.device,ue.ip_address "
                    + "FROM UserEvents ue "
                    +"INNER JOIN Users u on u.id=ue.user_id "
                    +"INNER JOIN Events e on e.id=ue.event_id "
                    +"WHERE ue.user_id=:userId and ue.deleted=0";
}
