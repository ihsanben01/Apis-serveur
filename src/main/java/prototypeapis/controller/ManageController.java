package prototypeapis.controller;

import prototypeapis.model.Deposit;
import prototypeapis.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/manage")
@CrossOrigin(origins = "*")
public class ManageController {

    private final DepositService depositService;

    @Autowired
    public ManageController(DepositService depositService) {
        this.depositService = depositService;
    }

    /**
     * Renouvelle un dépôt via token de gestion
     */
    @PostMapping("/{manageToken}/renew")
    public ResponseEntity<Void> renewDepositWithToken(@PathVariable String manageToken) {
        Optional<Deposit> depositOpt = depositService.getDepositByManageToken(manageToken);

        if (depositOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Deposit deposit = depositOpt.get();
        boolean renewed = depositService.renewDeposit(deposit.getId());

        return renewed ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    /**
     * Supprime un dépôt via token de gestion
     */
    @DeleteMapping("/{manageToken}")
    public ResponseEntity<Void> deleteDepositWithToken(@PathVariable String manageToken) {
        Optional<Deposit> depositOpt = depositService.getDepositByManageToken(manageToken);

        if (depositOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Deposit deposit = depositOpt.get();
        boolean deleted = depositService.deleteDeposit(deposit.getId());

        return deleted ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    /**
     * Récupère les infos d'un dépôt via token de gestion
     */
    @GetMapping("/{manageToken}")
    public ResponseEntity<?> getDepositInfoWithToken(@PathVariable String manageToken) {
        Optional<Deposit> depositOpt = depositService.getDepositByManageToken(manageToken);

        if (depositOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Deposit deposit = depositOpt.get();
        String response = String.format(
                "{\"fileName\": \"%s\", \"expirationDate\": \"%s\"}",
                deposit.getFileName(),
                deposit.getExpirationDate()
        );

        return ResponseEntity.ok().body(response);
    }
}