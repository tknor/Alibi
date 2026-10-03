package cz.tomas.alibi.common.repository;

import java.util.UUID;

public interface OperationSummaryProjection {

    UUID getId();

    String getCodeName();

    long getCrewSize();

    Integer getCrewSizeLimit();
}
