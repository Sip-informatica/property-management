package es.sipinformatica.propertymanagement.security.api.resources.restcontroller;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.sipinformatica.propertymanagement.security.api.dtos.UserDto;
import es.sipinformatica.propertymanagement.security.api.httpserrors.MessageResponse;
import es.sipinformatica.propertymanagement.security.domain.services.AdminService;

@RestController
@RequestMapping("/api/admin" + AdminResource.ADMIN)
public class AdminResource {
    public static final String ADMIN = "/users-admin";
    public static final String MOBILE_ID = "/phone/{mobile}";
    public static final String EMAIL = "/email/{email}";
    public static final String DNI = "/dni/{dni}";
    private static final String USERNAME = "/username/{username}";

    private AdminService adminService;

    @Autowired
    public AdminResource(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Stream<UserDto> readAll() {
        return this.adminService.readAll().map(UserDto::ofUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto creationUserDto) {
        creationUserDto.doDefault();
        Set<String> roles = creationUserDto.RolesUserDto();
        this.adminService.create(creationUserDto.toUser(), roles);

        return ResponseEntity
                .ok(new MessageResponse(creationUserDto.getUsername() + " User registered successfully, Role: "
                        + roles.stream().collect(Collectors.toList())));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(MOBILE_ID)
    public void updateByMobile(@Valid @RequestBody UserDto updateUserDto, @PathVariable String mobile) {
        this.adminService.updateByMobile(mobile, updateUserDto.toUser());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(MOBILE_ID)
    public UserDto readByMobile(@PathVariable String mobile) {

        return new UserDto(this.adminService.readByMobile(mobile));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(MOBILE_ID)
    public void deleteByMobile(@PathVariable String mobile) {
        this.adminService.delete(this.adminService.readByMobile(mobile));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(EMAIL)
    public ResponseEntity<Object> updateByEmail(@Valid @RequestBody UserDto updateUserDto, @PathVariable String email) {
        this.adminService.updateByEmail(email, updateUserDto.toUser());

        return ResponseEntity.ok(new MessageResponse(email + " - User updated successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(EMAIL)
    public UserDto readUserByEmail(@PathVariable String email) {

        return new UserDto(this.adminService.readByEmail(email));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(EMAIL)
    public ResponseEntity<Object> deleteByEmail(@PathVariable String email) {
        this.adminService.delete(this.adminService.readByEmail(email));

        return ResponseEntity.ok(new MessageResponse(email + " - User deleted successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(DNI)
    public void updateByDni(@Valid @RequestBody UserDto updateUserDto, @PathVariable String dni) {
        this.adminService.updateByEmail(dni, updateUserDto.toUser());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(DNI)
    public UserDto readUserByDni(@PathVariable String dni) {

        return new UserDto(this.adminService.readByDni(dni));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(DNI)
    public void deleteByDni(@PathVariable String dni) {
        this.adminService.delete(this.adminService.readByDni(dni));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(USERNAME)
    public void updateByUsername(@Valid @RequestBody UserDto updateUserDto, @PathVariable String username) {
        this.adminService.updateByUsername(username, updateUserDto.toUser());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(USERNAME)
    public UserDto readUserByUsername(@PathVariable String username) {

        return new UserDto(this.adminService.readByUsername(username));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(USERNAME)
    public void deleteByUsername(@PathVariable String username) {
        this.adminService.delete(this.adminService.readByUsername(username));
    }

}
