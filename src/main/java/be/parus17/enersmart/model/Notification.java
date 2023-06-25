package be.parus17.enersmart.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Notification {
    private String value1;
    private String value2;
    private String value3;

}