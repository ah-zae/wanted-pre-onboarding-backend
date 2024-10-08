package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JobPostingReq {

    @NotNull(message = "회사 ID는 필수입니다.")
    private Long companyId;

    @NotBlank(message = "채용포지션은 필수입니다.")
    private String position;

    @Min(value = 0,message = "채용보상금은 0원 이상이여야합니다.")
    private int reward;

    @NotBlank(message = "채용내용은 필수 입니다.")
    private String description;

    @NotBlank(message = "사용기술은 필수 입니다.")
    private String techStack;
}
