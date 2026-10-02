package cz.tomas.alibi.common.dto;

import java.util.UUID;

public record PersonSummaryDto(
        UUID id,
        String name
) {

}
