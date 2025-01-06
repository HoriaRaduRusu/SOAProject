package org.example.authenticationserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwkSetResponseDTO {
    private List<JwkResponseDTO> keys;

    @Data
    @Builder
    public static class JwkResponseDTO {
        private String kty;
        private String n;
        private String e;
        private String alg;
        private String use;
    }
}
