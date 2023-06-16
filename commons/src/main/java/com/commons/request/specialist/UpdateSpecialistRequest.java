package com.commons.request.specialist;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateSpecialistRequest extends SpecialistRequest {
    private String specialistName;
}
