package com.reservas.reservas_api.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    private long totalReservasHoy;
    private String salaMayorDemanda;

    // Datos para Gráficos
    private Map<String, Long> reservasPorSalaSemana; // Sala -> Cantidad
    private Map<Integer, Long> horasPico; // Hora del día -> Cantidad de reservas
}
