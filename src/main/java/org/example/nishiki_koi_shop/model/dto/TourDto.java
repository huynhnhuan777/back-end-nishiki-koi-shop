package org.example.nishiki_koi_shop.model.dto;

import lombok.Builder;
import lombok.Data;
import org.example.nishiki_koi_shop.model.entity.Tour;

import java.time.LocalDate;

@Data
@Builder
public class TourDto {
    private long TourId;
    private String TourName;
    private String TourDescription;
    private LocalDate TourStartDate;
    private LocalDate TourEndDate;
    private String image;
    private int TourCapacity;
    private long TourPrice;
    private long farmId;

    public static TourDto from(Tour tour) {
        return TourDto.builder()
                .TourId(tour.getTourId())
                .TourName(tour.getName())
                .TourDescription(tour.getDescription())
                .TourStartDate(tour.getStartDate())
                .TourEndDate(tour.getEndDate())
                .TourCapacity(tour.getMaxParticipants())
                .TourPrice(tour.getPrice())
                .image(tour.getImage())
                .farmId(tour.getFarm().getFarmId())
                .build();
    }
}
