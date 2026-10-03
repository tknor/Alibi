package cz.tomas.alibi.common.dto;

public record CreateOperationCommand(
        String codeName,
        Integer crewSizeLimit
) {
}
