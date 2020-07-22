package ibanvalidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class IbanController {

    private final Validator validator;

    @Autowired
    public IbanController(final Validator validator) {
        this.validator = validator;
    }

    private Map<String, Boolean> ibanResult = new HashMap<>();

    // Post raw JSON to http://localhost:8080/ using Postman
    // Format example
    //    [	"AA051245445454552117989",
    //            "LT647044001231465456",
    //            "LT517044077788877777",
    //            "LT227044077788877777",
    //            "CC051245445454552117989"]
    @PostMapping
    public ResponseEntity<Map<String, Boolean>> postList(@RequestBody List<String> ibanList) {
        ibanResult = validator.isValidIban(ibanList);
        return ResponseEntity.ok().body(ibanResult);
    }
    // Get http://localhost:8080/ using Postman
    @GetMapping
    public Map<String, Boolean> getResult() {
        return ibanResult;
    }

}
