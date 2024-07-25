package com.fpt.servicecontract.contract.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequestBody {
    int page;
    int size;
    String key;
}
