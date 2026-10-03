package cz.tomas.alibi.common.dto;

import java.util.UUID;

public record AddCrewMemberCommand(
        UUID operationId,
        UUID personId
) {
}
