package codesake.in.securecapita.dto;


import codesake.in.securecapita.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserEventsDTO extends Event {
    Long userId;
    String device;
    String ip_address;
    LocalDateTime created_At;
}
