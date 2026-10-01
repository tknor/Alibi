package cz.tomas.alibi.common.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Person {

    private String name;
    private String phone;
}
