package dev.wms.pwrapi.dto.isjsosdown;

import lombok.*;

import java.util.List;

public record InitialStatsDTO(List<InitialServiceStatsDTO> runningServices,
                              List<InitialDownServiceStatsDTO> downServices, String meme) {

}
