package cz.tomas.alibi.common.dto;

public record CreatePersonCommand(
        String name,
        String phone
) {

}
