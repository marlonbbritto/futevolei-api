package com.futevolei.championship.futevolei_api.dto.player;

import com.futevolei.championship.futevolei_api.model.Team;
import com.futevolei.championship.futevolei_api.model.enums.Registrations;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PlayerPaymentUpdateDto(
        @NotNull(message = "O status do pagamento é obrigatorio")
        Registrations registrations


) {
}
