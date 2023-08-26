package com.commons.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SetWebHookResult {
    private boolean ok;
    private boolean result;
    private String description;
}
