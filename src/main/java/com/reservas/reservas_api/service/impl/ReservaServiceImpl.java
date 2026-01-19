package com.reservas.reservas_api.service.impl;

import java.sql.Timestamp;

import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.reservas.reservas_api.dto.CancelarReservaRequestDto;
import com.reservas.reservas_api.dto.CrearReservaRequestDto;
import com.reservas.reservas_api.exception.BadRequestException;
import com.reservas.reservas_api.exception.ConflictException;
import com.reservas.reservas_api.exception.ResourceNotFoundException;
import com.reservas.reservas_api.service.IReservaService;

@Service
public class ReservaServiceImpl implements IReservaService {

    private final JdbcTemplate jdbcTemplate;

    public ReservaServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void crearReserva(CrearReservaRequestDto request) {

        jdbcTemplate.execute(
            "{ call pkg_reservas.pr_crear_reserva(?,?,?,?,?,?) }",
            (CallableStatementCallback<Void>) cs -> {

                cs.setLong(1, request.getIdSala());
                cs.setLong(2, request.getIdUsuario());
                cs.setDate(3, new java.sql.Date(Timestamp.valueOf(request.getFechaInicio()).getTime()));
                cs.setDate(4, new java.sql.Date(Timestamp.valueOf(request.getFechaFin()).getTime()));
                
                cs.registerOutParameter(5, java.sql.Types.NUMERIC);
                cs.registerOutParameter(6, java.sql.Types.VARCHAR);

                cs.execute();
                
                int codigo = cs.getInt(5);
                String mensaje = cs.getString(6);
                
                if (codigo != 0) {
                    handleError(codigo, mensaje);
                }
                
                return null;
            }
        );
    }

    @Override
    public void cancelarReserva(CancelarReservaRequestDto request) {

        jdbcTemplate.execute(
            "{ call pkg_reservas.pr_cancelar_reserva(?,?,?,?) }",
            (CallableStatementCallback<Void>) cs -> {

                cs.setLong(1, request.getIdReserva());
                cs.setString(2, request.getMotivo());
                
                cs.registerOutParameter(3, java.sql.Types.NUMERIC);
                cs.registerOutParameter(4, java.sql.Types.VARCHAR);

                cs.execute();
                
                int codigo = cs.getInt(3);
                String mensaje = cs.getString(4);
                
                if (codigo != 0) {
                    handleError(codigo, mensaje);
                }
                
                return null;
            }
        );
    }

    private void handleError(int codigo, String mensaje) {
        switch (codigo) {
            case 1:
            case 3:
                throw new BadRequestException(mensaje);
            case 2:
            case 4:
                throw new ConflictException(mensaje);
            case 5:
            case 6:
            case 7:
                throw new ResourceNotFoundException(mensaje);
            default:
                throw new RuntimeException(mensaje);
        }
    }
}

