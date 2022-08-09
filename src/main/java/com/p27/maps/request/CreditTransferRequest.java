package com.p27.maps.request;

import com.sun.istack.internal.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditTransferRequest {

    @NotNull
    private String mgsId;

    @Override
    public String toString() {
        return "CreditTransferRequest{" +
                "mgsId='" + mgsId + '\'' +
                '}';
    }
}
