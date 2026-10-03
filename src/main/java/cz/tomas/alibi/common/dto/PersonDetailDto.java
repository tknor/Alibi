package cz.tomas.alibi.common.dto;

import java.util.List;
import java.util.UUID;

public record PersonDetailDto(
        UUID id,
        String name,
        String phone,
        List<String> operationCodeNames
) {

}
