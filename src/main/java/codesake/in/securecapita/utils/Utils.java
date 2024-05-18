package codesake.in.securecapita.utils;

import codesake.in.securecapita.dto.UserEventsDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class Utils {

    public static  UserEventsDTO getHost(HttpServletRequest request){
        UserEventsDTO userEventsDTO = new UserEventsDTO();
        try{
            userEventsDTO.setIp_address(request.getRemoteAddr());
            if(userEventsDTO.getIp_address().equals("0:0:0:0:0:0:0:1") || userEventsDTO.getIp_address().equals("127.0.0.1")) {
                InetAddress hostAddress = InetAddress.getLocalHost();
                userEventsDTO.setIp_address(hostAddress.getHostAddress());
                userEventsDTO.setDevice(hostAddress.getHostName());
            }else{
                userEventsDTO.setDevice(request.getRemoteUser());
            }
        } catch (UnknownHostException e) {
            log.info("got unknown host");
            userEventsDTO.setDevice("unknown");
            userEventsDTO.setIp_address("unknown");
        }
        return userEventsDTO;
    }
}
