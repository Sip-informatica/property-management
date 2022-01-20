package es.sipinformatica.propertymanagement.security.api.resources.restcontroller;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
    private static final String MOBILE_ID = "/phone/{mobile}";
    private static final String EMAIL = "/email/{email}";
    private static final String DNI = "/dni/{dni}";
    private static final String USERNAME = "/username/{username}";

    private static final String DELETED = " - User deleted successfully";
    private static final String UPDATED = " - User updated successfully";

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
        Set<String> roles = creationUserDto.rolesUserDto();
        this.adminService.create(creationUserDto.toUser(), roles);

        return ResponseEntity.ok(new MessageResponse(creationUserDto.getUsername()
                + " User registered successfully, Role: " + roles.stream().collect(Collectors.toList())));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(MOBILE_ID)
    public ResponseEntity<Object> updateByMobile(@Valid @RequestBody UserDto updateUserDto, @PathVariable String mobile) {
        Set<String> roles = updateUserDto.rolesUserDto();
        this.adminService.updateByMobile(mobile, updateUserDto.updateUser(), roles);

        return ResponseEntity.ok(new MessageResponse(mobile + UPDATED));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(MOBILE_ID)
    public UserDto readByMobile(@PathVariable String mobile) {

        return new UserDto(this.adminService.readByMobile(mobile));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(MOBILE_ID)
    public ResponseEntity<Object> deleteByMobile(@PathVariable String mobile) {
        this.adminService.delete(this.adminService.readByMobile(mobile));

        return ResponseEntity.ok(new MessageResponse(mobile + DELETED));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(EMAIL)
    public ResponseEntity<Object> updateByEmail(@Valid @RequestBody UserDto updateUserDto, @PathVariable String email) {
        Set<String> roles = updateUserDto.rolesUserDto();
        this.adminService.updateByEmail(email, updateUserDto.updateUser(), roles);

        return ResponseEntity.ok(new MessageResponse(email + UPDATED));
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

        return ResponseEntity.ok(new MessageResponse(email + DELETED));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(DNI)
    public ResponseEntity<Object> updateByDni(@Valid @RequestBody UserDto updateUserDto, @PathVariable String dni) {
        Set<String> roles = updateUserDto.rolesUserDto();
        this.adminService.updateByDni(dni, updateUserDto.updateUser(), roles);

        return ResponseEntity.ok(new MessageResponse(dni + UPDATED));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(DNI)
    public UserDto readUserByDni(@PathVariable String dni) {

        return new UserDto(this.adminService.readByDni(dni));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(DNI)
    public ResponseEntity<Object> deleteByDni(@PathVariable String dni) {
        this.adminService.delete(this.adminService.readByDni(dni));

        return ResponseEntity.ok(new MessageResponse(dni + DELETED));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(USERNAME)
    public ResponseEntity<Object> updateByUsername(@Valid @RequestBody UserDto updateUserDto, @PathVariable String username) {
        Set<String> roles = updateUserDto.rolesUserDto();
        this.adminService.updateByUsername(username, updateUserDto.updateUser(), roles);

        return ResponseEntity.ok(new MessageResponse(username + UPDATED));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(USERNAME)
    public UserDto readUserByUsername(@PathVariable String username) {

        return new UserDto(this.adminService.readByUsername(username));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(USERNAME)
    public ResponseEntity<Object> deleteByUsername(@PathVariable String username) {
        this.adminService.delete(this.adminService.readByUsername(username));

        return ResponseEntity.ok(new MessageResponse(username + DELETED));
    }

}
