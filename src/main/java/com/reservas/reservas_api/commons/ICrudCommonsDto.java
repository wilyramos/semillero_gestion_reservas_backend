package com.reservas.reservas_api.commons;

public interface ICrudCommonsDto<DTOReq, DTORes, ID> {
    DTORes save(DTOReq request);
    DTORes update(ID id, DTOReq request);
    DTORes findById(ID id);
    DTORes delete(ID id); 
}