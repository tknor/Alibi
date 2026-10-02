package cz.tomas.alibi.common.dto;

import java.util.List;
import java.util.UUID;

public record OperationSummaryDto(
        UUID id,
        String codeName,
        Integer crewSize,
        Integer crewSizeLimit
) {
}
