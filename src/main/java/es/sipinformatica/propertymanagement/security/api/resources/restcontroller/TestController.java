package es.sipinformatica.propertymanagement.security.api.resources.restcontroller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/authenticated")
	@PreAuthorize("hasRole('MANAGER') or hasRole('OPERATOR') or hasRole('CUSTOMER') or hasRole('ADMIN')")
	public String userAccess() {
		return "User authenticated Content.";
	}

	@GetMapping("/customer")
	@PreAuthorize("hasRole('CUSTOMER')")
	public String customerAccess() {
		return "Customer Board.";
    }
    
    @GetMapping("/operator")
	@PreAuthorize("hasRole('OPERATOR')")
	public String operatorAccess() {
		return "Operator Board.";
    }

    @GetMapping("/manager")
	@PreAuthorize("hasRole('MANAGER')")
	public String managerAccess() {
		return "Manager Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
}
