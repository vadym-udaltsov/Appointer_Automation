package com.commons.request.specialist;

import com.commons.model.Specialist;
import com.commons.request.DepartmentRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpecialistRequest extends DepartmentRequest {
    private Specialist specialist;
}
