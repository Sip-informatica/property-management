package es.sipinformatica.propertymanagement.security.api.resources.restcontroller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.sipinformatica.propertymanagement.security.domain.exceptions.ResourceNotFoundException;

@RestController
@RequestMapping("/api/test")
public class TestController {
	public static final String EXCEPTIONS = "/exception";
	public static final String ID_ID = "/{id}";
	
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	@GetMapping("/adminmanager")
	@PreAuthorize("hasRole('MANAGER') or  hasRole('ADMIN')")
	public String adminmanagerAccess() {
		return "AdminManager Board.";
	}
	
	@GetMapping("/authenticated")
	@PreAuthorize("hasRole('MANAGER') or hasRole('OPERATOR') or hasRole('CUSTOMER') or hasRole('ADMIN') or hasRole('AUTHENTICATED ')")
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
	
	@GetMapping(EXCEPTIONS + ID_ID)
	public void read(@PathVariable int id) throws Exception {
		if (id < 1){
			throw new ResourceNotFoundException("id: " + id);			
		} else {
			throw new Exception();
		}
	}
	
	@GetMapping(EXCEPTIONS + "/handleall" )
	public void handleAll() throws Exception {		
			throw new Exception();			
	}
	
}
