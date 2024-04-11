package com.agitex.climax.dtos;

import com.agitex.climax.entities.AbstractAuditingEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class ProfessionSalaireDTO extends AbstractAuditingEntity {
    private String profession;
    private BigDecimal salaireMoyen;
}
