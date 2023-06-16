package com.commons.request.specialist;

import com.commons.request.DepartmentRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteSpecialistRequest extends DepartmentRequest {
    private String specialistName;
}
