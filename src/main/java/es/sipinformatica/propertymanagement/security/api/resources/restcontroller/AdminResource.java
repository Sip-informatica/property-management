package es.sipinformatica.propertymanagement.security.api.resources.restcontroller;

import java.util.Set;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import es.sipinformatica.propertymanagement.security.domain.services.AdminService;

@RestController
@RequestMapping("/api/auth" + AdminResource.ADMIN)
public class AdminResource {
    public static final String ADMIN = "/users-admin";
    public static final String MOBILE_ID = "/phone/{mobile}";
    public static final String EMAIL = "/email/{email}";
    public static final String DNI = "/dni/{dni}";
    public static final String ROLE = "/role/{role}";

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
    public void create(@Valid @RequestBody UserDto creationUserDto) {
        creationUserDto.doDefault();
        Set<String> roles = creationUserDto.roles();
        this.adminService.create(creationUserDto.toUser(), roles);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(MOBILE_ID)
    public void updateByMobile(@Valid @RequestBody UserDto updateUserDto, @PathVariable String mobile) {
        this.adminService.updateByMobile(mobile, updateUserDto.toUser());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(MOBILE_ID)
    public UserDto readUser(@PathVariable String mobile) {

        return new UserDto(this.adminService.readByMobile(mobile));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(MOBILE_ID)
    public void deleteByMobile(@PathVariable String mobile) {
        this.adminService.delete(this.adminService.readByMobile(mobile));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(EMAIL)
    public void updateByEmail(@Valid @RequestBody UserDto updateUserDto, @PathVariable String email) {
        this.adminService.updateByEmail(email, updateUserDto.toUser());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(EMAIL)
    public UserDto readUserByEmail(@PathVariable String email) {

        return new UserDto(this.adminService.readByEmail(email));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(EMAIL)
    public void deleteByEmail(@PathVariable String email) {
        this.adminService.delete(this.adminService.readByEmail(email));
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

}
