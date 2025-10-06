package prototypeapis.controller;

import prototypeapis.dto.CreateDepositRequest;
import prototypeapis.dto.DepositResponse;
import prototypeapis.service.DepositService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/deposits")
@CrossOrigin(origins = "*") // À configurer proprement en production
public class DepositController {

    private final DepositService depositService;

    @Autowired
    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    /**
     * Crée un nouveau dépôt
     */
    @PostMapping
    public ResponseEntity<DepositResponse> createDeposit(@Valid @RequestBody CreateDepositRequest request) {
        try {
            DepositResponse response = depositService.createDeposit(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Récupère les informations d'un dépôt
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepositResponse> getDepositInfo(@PathVariable UUID id) {
        Optional<DepositResponse> depositOpt = depositService.getDepositInfo(id);
        return depositOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Renouvelle la durée de vie d'un dépôt
     */
    @PostMapping("/{id}/renew")
    public ResponseEntity<Void> renewDeposit(@PathVariable UUID id) {
        boolean renewed = depositService.renewDeposit(id);
        return renewed ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * Supprime un dépôt
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeposit(@PathVariable UUID id) {
        boolean deleted = depositService.deleteDeposit(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}