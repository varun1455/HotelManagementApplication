package com.project.stayEase.advices;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ApiError {
    private String message;

    @Builder.Default
    private List<String> subErrors = new ArrayList<>();

}
