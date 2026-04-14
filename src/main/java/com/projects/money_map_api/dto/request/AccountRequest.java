package com.projects.money_map_api.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {
    private String name;
    private String currency;
    private String description;
    private String category;
}
