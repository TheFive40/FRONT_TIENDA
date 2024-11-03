package io.github.thefive40.tienda_front.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountCodeDTO {
    private Long idDiscount;

    private String code;

    private double percentage;

    private Date startDate;

    private Date endDate;
}
