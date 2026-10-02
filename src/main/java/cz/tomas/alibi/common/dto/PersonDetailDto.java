package cz.tomas.alibi.common.dto;

import java.util.UUID;

// TODO add and implement getting all the operations where the person is involved
public record PersonDetailDto(
        UUID id,
        String name,
        String phone
) {

}
