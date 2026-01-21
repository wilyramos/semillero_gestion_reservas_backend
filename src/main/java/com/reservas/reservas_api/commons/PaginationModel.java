package com.reservas.reservas_api.commons;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// PaginationModel.java
public class PaginationModel {
    private Integer pageNumber;
    private Integer rowsPerPage;
    private List<SortModel> sorts;
    private List<FilterModel> filters;
}